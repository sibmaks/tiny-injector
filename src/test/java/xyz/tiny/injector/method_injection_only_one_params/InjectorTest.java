package xyz.tiny.injector.method_injection_only_one_params;

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
     * IllegalStateException expected in case of method injection accept more than 1 param
     */
    @Test
    void injectedMethodShouldHaveOnlyOneParam() {
        Assertions.assertThrows(MethodInjectionException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}