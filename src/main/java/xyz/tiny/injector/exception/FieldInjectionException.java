package xyz.tiny.injector.exception;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
public class FieldInjectionException extends RuntimeException {
    public FieldInjectionException(String message) {
        super(message);
    }
}