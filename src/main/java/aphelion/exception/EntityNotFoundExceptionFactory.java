package aphelion.exception;

public interface EntityNotFoundExceptionFactory {
    EntityNotFoundException create(Class entityClass);
    EntityNotFoundException create(Class entityClass, Long entityId);
}
