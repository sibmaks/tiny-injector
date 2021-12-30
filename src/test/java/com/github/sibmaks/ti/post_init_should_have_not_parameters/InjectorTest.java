package com.github.sibmaks.ti.post_init_should_have_not_parameters;

import com.github.sibmaks.ti.Injector;
import com.github.sibmaks.ti.exception.InitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void postInitMethodShouldBeWithoutParameters() {
        String name = InjectorTest.class.getPackage().getName();
        InitializationException exception = Assertions.assertThrows(InitializationException.class, () -> Injector.buildInjections(name));
        Assertions.assertInstanceOf(IllegalStateException.class, exception.getCause());
    }
}