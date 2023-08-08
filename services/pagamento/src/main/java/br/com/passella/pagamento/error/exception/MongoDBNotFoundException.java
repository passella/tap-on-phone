package br.com.passella.pagamento.error.exception;

public class MongoDBNotFoundException extends RepositoryException {
    public MongoDBNotFoundException(final String message) {
        super(message);
    }
}
