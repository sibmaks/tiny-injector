package com.github.sibmaks.ti.provider_exception_on_get;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void providerCanThrowExceptionOnGet() {
        String name = InjectorTest.class.getPackage().getName();
        Assertions.assertThrows(RuntimeException.class, () -> Injector.buildInjections(name));
    }
}