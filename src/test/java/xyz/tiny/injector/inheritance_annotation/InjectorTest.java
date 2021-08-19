package xyz.tiny.injector.inheritance_annotation;

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
    void inheritanceAnnotation() throws Throwable {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AbstractComponent abstractComponent = context.getComponent("aComponent");
        Assertions.assertNotNull(abstractComponent);
        AComponent aComponent = context.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);

        Assertions.assertEquals(abstractComponent, aComponent);
        Assertions.assertEquals(abstractComponent.aComponent, aComponent);
    }
}