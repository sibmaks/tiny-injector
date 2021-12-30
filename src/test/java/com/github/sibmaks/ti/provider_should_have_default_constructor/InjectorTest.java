package com.github.sibmaks.ti.provider_should_have_default_constructor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void providerShouldHaveDefaultConstructor() {
        String name = InjectorTest.class.getPackage().getName();
        Assertions.assertThrows(NoSuchMethodException.class, () -> Injector.buildInjections(name));
    }
}