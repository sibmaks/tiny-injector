package xyz.tiny.injector.component_definition;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.ComponentDefinition;
import xyz.tiny.injector.Injector;
import xyz.tiny.injector.context.IContext;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
class InjectorTest {

    @Test
    void componentDefinitionCustomizedBeansRejected() throws Throwable {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());

        ComponentDefinition<AComponent> componentDefinition = context.getComponentDefinition("aComponent");

        Assertions.assertEquals("dog", componentDefinition.getComponentInstance().name);
        Assertions.assertEquals("cat", componentDefinition.getComponentBaseInstance().name);

        AComponent aComponent = context.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);
        BComponent bComponent = context.getComponent("bComponent");
        Assertions.assertNotNull(bComponent);
        Assertions.assertEquals("dog", aComponent.name);
        Assertions.assertEquals("dog", bComponent.aComponent.name);
        Assertions.assertEquals(aComponent, bComponent.aComponent);
        Assertions.assertEquals(bComponent, aComponent.bComponent);
    }
}