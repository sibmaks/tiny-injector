package xyz.tiny.injector.method_inject_should_at_least_one_param;

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
     * MethodInjectionException expected in case of method injection without params
     */
    @Test
    void methodInjection() {
        Assertions.assertThrows(MethodInjectionException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}