package xyz.tiny.injector.provide_named_component;

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
    void providerCanChangeComponentName() throws Throwable {
        IContext iContext = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AComponent aComponent = iContext.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);

        BComponent bComponent = iContext.getComponent("cComponent");
        Assertions.assertNotNull(aComponent);

        Assertions.assertEquals(bComponent, aComponent.cComponent);
    }
}