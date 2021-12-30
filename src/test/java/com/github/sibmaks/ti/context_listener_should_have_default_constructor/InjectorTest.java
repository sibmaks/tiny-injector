package com.github.sibmaks.ti.context_listener_should_have_default_constructor;

import com.github.sibmaks.ti.exception.InitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
class InjectorTest {

    @Test
    void contextListenerShouldHaveDefaultConstructor() {
        String name = InjectorTest.class.getPackage().getName();
        InitializationException exception = Assertions.assertThrows(InitializationException.class, () -> Injector.buildInjections(name));
        Assertions.assertTrue(exception.getCause() instanceof NoSuchMethodException);
    }
}