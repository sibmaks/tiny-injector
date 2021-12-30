package com.github.sibmaks.ti.method_injection_only_component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;
import com.github.sibmaks.ti.exception.MethodInjectionException;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    /**
     * IllegalStateException expected in case of injected component is not exists
     */
    @Test
    void onlyExistedComponentCanBeInjected() {
        String name = InjectorTest.class.getPackage().getName();
        Assertions.assertThrows(MethodInjectionException.class, () -> Injector.buildInjections(name));
    }
}