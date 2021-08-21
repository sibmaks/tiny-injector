package xyz.tiny.injector.field_injection_list_as_component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.Injector;
import xyz.tiny.injector.context.IContext;

import java.util.List;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void listCanBeProviderAsComponent() throws Throwable {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AComponent aComponent = context.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);
        List<BComponent> bComponents = context.getComponent("bComponents");
        Assertions.assertNotNull(bComponents);

        Assertions.assertEquals(1, bComponents.size());
        Assertions.assertEquals(aComponent.bComponents, bComponents);
    }
}