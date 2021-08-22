package xyz.tiny.injector.post_init_simple;

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
    void postInitSimple() throws Throwable {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AComponent aComponent = context.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);
        BComponent bComponent = context.getComponent("bComponent");
        Assertions.assertNotNull(bComponent);

        Assertions.assertEquals(bComponent, aComponent.bComponent);
        Assertions.assertEquals(bComponent.content, aComponent.content);
    }
}