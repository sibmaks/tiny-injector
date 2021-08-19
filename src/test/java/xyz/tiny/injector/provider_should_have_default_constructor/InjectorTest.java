package xyz.tiny.injector.provider_should_have_default_constructor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.Injector;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void providerShouldHaveDefaultConstructor() {
        Assertions.assertThrows(NoSuchMethodException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}