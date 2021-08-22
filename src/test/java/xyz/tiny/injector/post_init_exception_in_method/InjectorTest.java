package xyz.tiny.injector.post_init_exception_in_method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.Injector;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void postInitExceptionInMethod() {
        Assertions.assertThrows(RuntimeException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}