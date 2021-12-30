package com.github.sibmaks.ti.no_interface_impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;

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
        Assertions.assertThrows(IllegalStateException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}