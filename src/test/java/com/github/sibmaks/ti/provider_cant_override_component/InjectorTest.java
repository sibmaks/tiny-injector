package com.github.sibmaks.ti.provider_cant_override_component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void providerCantOverrideComponent() {
        String name = InjectorTest.class.getPackage().getName();
        Assertions.assertThrows(IllegalStateException.class, () -> Injector.buildInjections(name));
    }
}