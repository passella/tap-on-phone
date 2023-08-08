package br.com.passella.cadastro.error.exception;

public class PostgreSQLNotFoundException extends RepositoryException {
    public PostgreSQLNotFoundException(final String message) {
        super(message);
    }
}
