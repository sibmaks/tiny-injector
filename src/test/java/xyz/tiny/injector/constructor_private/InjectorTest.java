package xyz.tiny.injector.constructor_private;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.Injector;
import xyz.tiny.injector.context.IContext;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void privateConstructor() throws Throwable {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        Assertions.assertNotNull(context.getComponent("aComponent"));
    }
}