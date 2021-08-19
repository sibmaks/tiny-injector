package xyz.tiny.injector.field_injection_final;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.Injector;
import xyz.tiny.injector.exception.FieldInjectionException;

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