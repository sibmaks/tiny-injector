package com.github.sibmaks.ti.exception;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
public class FieldInjectionException extends InitializationException {
    public FieldInjectionException(String message) {
        super(message);
    }

    public FieldInjectionException(Throwable throwable) {
        super(throwable);
    }
}
