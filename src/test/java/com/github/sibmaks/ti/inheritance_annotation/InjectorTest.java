package com.github.sibmaks.ti.inheritance_annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;
import com.github.sibmaks.ti.context.IContext;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void inheritanceAnnotation() {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AbstractComponent abstractComponent = context.getComponent("aComponent");
        Assertions.assertNotNull(abstractComponent);
        AComponent aComponent = context.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);

        Assertions.assertEquals(abstractComponent, aComponent);
        Assertions.assertEquals(abstractComponent.aComponent, aComponent);
    }
}