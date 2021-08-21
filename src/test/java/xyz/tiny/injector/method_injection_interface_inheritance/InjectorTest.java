package xyz.tiny.injector.method_injection_interface_inheritance;

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
    void methodInjectionsSaveAnnotationsFromInterface() throws Throwable {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        IComponent component = context.getComponent("aComponent");
        Assertions.assertNotNull(component);

        AComponent aComponent = context.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);
        Assertions.assertEquals(aComponent, component);

        BComponent bComponent1 = context.getComponent("bComponent1");
        Assertions.assertNotNull(bComponent1);
        BComponent bComponent2 = context.getComponent("bComponent2");
        Assertions.assertNotNull(bComponent2);

        Assertions.assertEquals(BComponent1.class, bComponent1.getClass());
        Assertions.assertEquals(BComponent2.class, bComponent2.getClass());

        Assertions.assertEquals(aComponent.bComponent1, bComponent1);
        Assertions.assertEquals(aComponent.bComponent2, bComponent2);
    }
}