package com.github.sibmaks.ti.no_interface_impl;

import com.github.sibmaks.ti.Injector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    /**
     * IllegalStateException excepted in case of @Component interface is not implemented
     */
    @Test
    void noInterfaceImplementation() {
        String name = InjectorTest.class.getPackage().getName();
        Assertions.assertThrows(IllegalStateException.class, () -> Injector.buildInjections(name));
    }
}