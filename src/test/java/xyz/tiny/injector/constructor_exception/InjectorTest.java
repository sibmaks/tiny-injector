package xyz.tiny.injector.constructor_exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.Injector;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    /**
     * Exception from constructor will be throwing up
     */
    @Test
    void noDefaultConstructorAndInjected() {
        Assertions.assertThrows(RuntimeException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}