package com.github.sibmaks.ti.post_init_exception_in_method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void postInitExceptionInMethod() {
        String name = InjectorTest.class.getPackage().getName();
        Assertions.assertThrows(RuntimeException.class, () -> Injector.buildInjections(name));
    }
}