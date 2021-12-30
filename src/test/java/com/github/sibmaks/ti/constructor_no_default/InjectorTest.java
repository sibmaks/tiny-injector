package com.github.sibmaks.ti.constructor_no_default;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void noDefaultConstructorAndInjected() {
        Assertions.assertThrows(NoSuchMethodException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}