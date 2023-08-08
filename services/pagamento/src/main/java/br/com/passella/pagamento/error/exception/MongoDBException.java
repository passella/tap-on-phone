package br.com.passella.pagamento.error.exception;

public class MongoDBException extends RepositoryException {
    public MongoDBException(final String message) {
        super(message);
    }
}
