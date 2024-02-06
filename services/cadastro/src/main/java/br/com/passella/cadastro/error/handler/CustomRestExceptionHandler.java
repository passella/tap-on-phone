package br.com.passella.cadastro.error.handler;

import br.com.passella.cadastro.error.ApiError;
import br.com.passella.cadastro.error.ApiErrorRequest;
import br.com.passella.cadastro.error.exception.PostgreSQLNotFoundException;
import br.com.passella.cadastro.service.kafka.DeadLetterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler implements WebExceptionHandler {

    private final Logger logger = Logger.getLogger(CustomRestExceptionHandler.class.getName());
    private final ObjectMapper mapper;
    private final DeadLetterService deadLetterService;


    public CustomRestExceptionHandler(final ObjectMapper mapper, final DeadLetterService deadLetterService) {
        this.mapper = mapper;
        this.deadLetterService = deadLetterService;
    }


    @ExceptionHandler({RuntimeException.class})
    public Mono<ResponseEntity<Object>> handleRuntimeException(final RuntimeException ex, final ServerWebExchange exchange) {
        logger.log(Level.SEVERE, ex.toString(), ex);
        final var errors = getThrowableList(ex).stream().map(Throwable::toString).toList();
        final var responseEntityMono = buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.toString(), errors, exchange);
        deadLetterService.send(responseEntityMono);
        return responseEntityMono;
    }


    @ExceptionHandler({PostgreSQLNotFoundException.class})
    public Mono<ResponseEntity<Object>> handlePostgreSQLNotFoundException(final PostgreSQLNotFoundException ex, final ServerWebExchange exchange) {
        logger.log(Level.SEVERE, ex.toString(), ex);
        final var errors = List.of(ex.toString());
        final var responseEntityMono = buildResponseEntity(HttpStatus.NOT_FOUND, ex.toString(), errors, exchange);
        deadLetterService.send(responseEntityMono);
        return responseEntityMono;
    }


    @ExceptionHandler({Exception.class})
    public Mono<ResponseEntity<Object>> handleException(final Exception ex) {
        logger.log(Level.SEVERE, ex.toString(), ex);
        final var errors = getThrowableList(ex).stream().map(Throwable::toString).toList();
        final var apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), errors, null);
        final Mono<ResponseEntity<Object>> responseEntityMono = Mono.just(new ResponseEntity<>(apiError, apiError.status()));
        deadLetterService.send(responseEntityMono);
        return responseEntityMono;
    }

    private static List<Throwable> getThrowableList(final Exception ex) {
        final List<Throwable> throwableList = new ArrayList<>();
        Throwable cause = ex;
        while (cause != null) {
            throwableList.add(cause);
            cause = cause.getCause();
        }
        return throwableList;
    }


    @Override
    protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(final WebExchangeBindException ex, final HttpHeaders headers, final HttpStatusCode status, final ServerWebExchange exchange) {
        logger.warning(ex.toString());
        final var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> MessageFormat.format("{0}: {1}", fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        final var responseEntityMono = buildResponseEntity(HttpStatus.valueOf(status.value()), ex.getBody().getDetail(), errors, exchange);
        deadLetterService.send(responseEntityMono);
        return responseEntityMono;
    }

    @Override
    protected Mono<ResponseEntity<Object>> handleExceptionInternal(final Exception ex, final Object body, final HttpHeaders headers, final HttpStatusCode status, final ServerWebExchange exchange) {
        logger.log(Level.SEVERE, ex.toString(), ex);
        final var errors = getThrowableList(ex).stream().map(Throwable::toString).toList();
        final var responseEntityMono = buildResponseEntity(HttpStatus.valueOf(status.value()), ex.toString(), errors, exchange);
        deadLetterService.send(responseEntityMono);
        return responseEntityMono;
    }

    @ExceptionHandler({BulkheadFullException.class, RequestNotPermitted.class})
    public Mono<ResponseEntity<Object>> handleBulkheadFullException(final RuntimeException ex, final ServerWebExchange exchange) {
        logger.warning(ex.toString());
        final var responseEntityMono = buildResponseEntity(HttpStatus.TOO_MANY_REQUESTS, ex, exchange);
        deadLetterService.send(responseEntityMono);
        return responseEntityMono;
    }

    private Mono<ResponseEntity<Object>> buildResponseEntity(final HttpStatus status, final String message, final List<String> errors, final ServerWebExchange exchange) {
        if (exchange != null) {
            final var request = exchange.getRequest();
            final var id = request.getId();
            final var path = request.getPath().toString();
            final var method = request.getMethod().toString();
            final var headers = request.getHeaders()
                    .entrySet()
                    .stream()
                    .map(entry -> (Map.Entry<String, String>) new SimpleEntry<>(entry.getKey(), String.join(", ", entry.getValue())))
                    .toList();
            final var apiError = new ApiError(status, message, errors, new ApiErrorRequest(id, path, method, headers));
            return Mono.just(new ResponseEntity<>(apiError, apiError.status()));
        }

        final var apiError = new ApiError(status, message, errors, null);
        return Mono.just(new ResponseEntity<>(apiError, apiError.status()));

    }

    private Mono<ResponseEntity<Object>> buildResponseEntity(final HttpStatus status, final RuntimeException ex, final ServerWebExchange exchange) {
        final var errors = getThrowableList(ex).stream().map(Throwable::toString).toList();
        return buildResponseEntity(status, ex.toString(), errors, exchange);
    }

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable ex) {
        if (ex instanceof final ResponseStatusException responseStatusException && responseStatusException.getStatusCode().value() == 404) {
            final var responseEntityMono = buildResponseEntity(HttpStatus.NOT_FOUND, responseStatusException, exchange);
            deadLetterService.send(responseEntityMono);
            return responseEntityMono.flatMap(responseEntity -> {
                final var response = exchange.getResponse();
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                response.setStatusCode(responseEntity.getStatusCode());
                final var body = responseEntity.getBody();
                if (body == null) {
                    return Mono.empty();
                }
                final var bufferFactory = response.bufferFactory();
                final DataBuffer dataBuffer;
                try {
                    dataBuffer = bufferFactory.wrap(mapper.writeValueAsBytes(body));
                } catch (final JsonProcessingException e) {
                    return Mono.error(e);
                }
                return response.writeWith(Mono.just(dataBuffer));
            });
        }
        return Mono.error(ex);
    }

}
