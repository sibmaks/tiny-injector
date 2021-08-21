package xyz.tiny.injector.exception;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
public class MethodInjectionException extends RuntimeException {
    public MethodInjectionException(String message) {
        super(message);
    }

    public MethodInjectionException(Throwable cause) {
        super(cause);
    }
}
