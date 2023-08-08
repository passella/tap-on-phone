package br.com.passella.cadastro.error.exception;

public class PostgreSQLException extends RepositoryException {
    public PostgreSQLException(final String message) {
        super(message);
    }
}
