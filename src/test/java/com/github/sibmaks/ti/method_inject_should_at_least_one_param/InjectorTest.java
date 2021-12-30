package com.github.sibmaks.ti.method_inject_should_at_least_one_param;

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
     * MethodInjectionException expected in case of method injection without params
     */
    @Test
    void methodInjection() {
        String name = InjectorTest.class.getPackage().getName();
        Assertions.assertThrows(MethodInjectionException.class, () -> Injector.buildInjections(name));
    }
}