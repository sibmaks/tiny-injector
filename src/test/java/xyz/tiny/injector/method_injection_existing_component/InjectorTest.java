package xyz.tiny.injector.method_injection_existing_component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.Injector;
import xyz.tiny.injector.exception.MethodInjectionException;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    /**
     * IllegalStateException expected in case of method injection when Named annotation contains invalid component name
     */
    @Test
    void namedAnnotationsShouldHaveValidName() {
        Assertions.assertThrows(MethodInjectionException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}