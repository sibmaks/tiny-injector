package xyz.tiny.injector.method_inject_exception_in_method;

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
     * MethodInjectionException expected in case of method injection without Named annotation
     */
    @Test
    void methodInjection() {
        Assertions.assertThrows(MethodInjectionException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}