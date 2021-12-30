package com.github.sibmaks.ti.method_inject_exception_in_method;

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
     * MethodInjectionException expected in case of method injection without Named annotation
     */
    @Test
    void methodInjection() {
        String name = InjectorTest.class.getPackage().getName();
        Assertions.assertThrows(MethodInjectionException.class, () -> Injector.buildInjections(name));
    }
}