package xyz.tiny.injector.field_inject_not_exists_component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.Injector;
import xyz.tiny.injector.exception.FieldInjectionException;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    /**
     * IllegalStateException expected in case of injecting not existing component
     */
    @Test
    void fieldInjectedComponentNotExists() {
        Assertions.assertThrows(FieldInjectionException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}