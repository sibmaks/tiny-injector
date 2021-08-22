package xyz.tiny.injector.post_init_should_have_not_parameters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.Injector;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void postInitMethodShouldBeWithoutParameters() {
        Assertions.assertThrows(IllegalStateException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}