package com.github.sibmaks.ti.field_injection_final;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;
import com.github.sibmaks.ti.exception.FieldInjectionException;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void cantInjectFinal() {
        Assertions.assertThrows(FieldInjectionException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}