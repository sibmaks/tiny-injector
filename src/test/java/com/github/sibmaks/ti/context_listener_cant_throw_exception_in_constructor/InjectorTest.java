package com.github.sibmaks.ti.context_listener_cant_throw_exception_in_constructor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
class InjectorTest {

    @Test
    void contextListenerConstructorCantThrowException() {
        Assertions.assertThrows(RuntimeException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}