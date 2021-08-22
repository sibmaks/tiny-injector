package xyz.tiny.injector.component_can_be_proxed;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.Injector;
import xyz.tiny.injector.context.IContext;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
class InjectorTest {

    @Test
    void componentCanBeProxy() throws Throwable {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AComponent aComponent = context.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);

        Assertions.assertEquals(0, StaticContext.proxyCalls.get());

        aComponent.method();
        Assertions.assertEquals(1, StaticContext.proxyCalls.get());

        aComponent.aComponent.method();
        Assertions.assertEquals(2, StaticContext.proxyCalls.get());
    }
}