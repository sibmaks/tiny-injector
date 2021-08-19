package xyz.tiny.injector.field_injection_named_changed;

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
    void modifedComponentNameInjection() throws Exception {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AComponent aComponent = context.getComponent("aComponent");
        BComponent bComponent = context.getComponent("cComponent");
        Assertions.assertNotNull(aComponent);
        Assertions.assertNotNull(bComponent);
        Assertions.assertEquals(bComponent, aComponent.bComponent);
    }
}