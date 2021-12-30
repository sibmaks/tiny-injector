package com.github.sibmaks.ti.field_injection_final;

import com.github.sibmaks.ti.exception.InitializationException;
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
        String name = InjectorTest.class.getPackage().getName();
        InitializationException exception = Assertions.assertThrows(InitializationException.class, () -> Injector.buildInjections(name));
        Assertions.assertInstanceOf(FieldInjectionException.class, exception.getCause());
    }
}