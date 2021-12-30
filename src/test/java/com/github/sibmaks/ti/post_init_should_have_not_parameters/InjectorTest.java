package com.github.sibmaks.ti.post_init_should_have_not_parameters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void postInitMethodShouldBeWithoutParameters() {
        String name = InjectorTest.class.getPackage().getName();
        Assertions.assertThrows(IllegalStateException.class, () -> Injector.buildInjections(name));
    }
}