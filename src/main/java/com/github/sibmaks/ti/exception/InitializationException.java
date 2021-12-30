package com.github.sibmaks.ti.exception;

public class InitializationException extends RuntimeException {
    public InitializationException(Throwable throwable) {
        super(throwable);
    }

    public InitializationException(String message) {
        super(message);
    }
}
