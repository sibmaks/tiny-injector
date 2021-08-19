package xyz.tiny.injector.method_injection_abstract_inheritance;

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
    void methodInjections() throws Exception {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AbstractComponent component = context.getComponent("aComponent");
        Assertions.assertNotNull(component);

        AComponent aComponent = context.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);
        Assertions.assertEquals(aComponent, component);

        BComponent bComponent1 = context.getComponent("bComponent1");
        BComponent bComponent2 = context.getComponent("bComponent2");
        BComponent bComponent3 = context.getComponent("bComponent3");

        Assertions.assertNotNull(bComponent1);
        Assertions.assertNotNull(bComponent2);
        Assertions.assertNotNull(bComponent3);

        Assertions.assertEquals(BComponent1.class, bComponent1.getClass());
        Assertions.assertEquals(BComponent2.class, bComponent2.getClass());
        Assertions.assertEquals(BComponent3.class, bComponent3.getClass());

        Assertions.assertEquals(aComponent.bComponent1, bComponent1);
        Assertions.assertEquals(aComponent.bComponent2, bComponent2);
        Assertions.assertEquals(aComponent.bComponent3, bComponent3);
    }
}