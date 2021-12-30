package com.github.sibmaks.ti.context_listener_should_have_default_constructor;

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
        Assertions.assertThrows(NoSuchMethodException.class, () -> Injector.buildInjections(name));
    }
}