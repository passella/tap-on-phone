package br.com.passella.motorpagamento.config;

import br.com.passella.motorpagamento.error.handle.kafka.ApplicationKafkaBackOff;
import br.com.passella.motorpagamento.error.handle.kafka.ApplicationKafkaConsumerRecordRecoverer;
import br.com.passella.motorpagamento.error.handle.kafka.ApplicationKafkaRecordInterceptor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;

import java.util.HashMap;
import java.util.Map;


@EnableKafka
@Configuration
public class KafkaConfig {
    private final String bootstrapAddress;
    private final String groupId;
    private final ApplicationKafkaRecordInterceptor applicationKafkaRecordInterceptor;
    private final ApplicationKafkaBackOff applicationKafkaBackOff;
    private final ApplicationKafkaConsumerRecordRecoverer applicationKafkaConsumerRecordRecoverer;

    public KafkaConfig(final ApplicationKafkaRecordInterceptor applicationKafkaRecordInterceptor,
                       final ApplicationKafkaBackOff applicationKafkaBackOff,
                       final ApplicationKafkaConsumerRecordRecoverer applicationKafkaConsumerRecordRecoverer,
                       @Value(value = "${spring.kafka.bootstrap-servers}") final String bootstrapAddress,
                       @Value(value = "${spring.kafka.consumer.group-id}") final String groupId) {
        this.applicationKafkaRecordInterceptor = applicationKafkaRecordInterceptor;
        this.applicationKafkaBackOff = applicationKafkaBackOff;
        this.applicationKafkaConsumerRecordRecoverer = applicationKafkaConsumerRecordRecoverer;
        this.bootstrapAddress = bootstrapAddress;
        this.groupId = groupId;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        final Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        final Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory() {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(Runtime.getRuntime().availableProcessors());
        factory.setCommonErrorHandler(new DefaultErrorHandler(applicationKafkaConsumerRecordRecoverer, applicationKafkaBackOff));
        factory.setRecordInterceptor(applicationKafkaRecordInterceptor);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return factory;
    }


}
