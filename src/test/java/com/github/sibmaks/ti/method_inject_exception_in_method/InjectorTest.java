package com.github.sibmaks.ti.method_inject_exception_in_method;

import com.github.sibmaks.ti.Injector;
import com.github.sibmaks.ti.exception.MethodInjectionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        MethodInjectionException exception = Assertions.assertThrows(MethodInjectionException.class, () -> Injector.buildInjections(name));
        Assertions.assertInstanceOf(RuntimeException.class, exception.getCause());
    }
}