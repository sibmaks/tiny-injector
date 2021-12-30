package com.github.sibmaks.ti.field_injection_several_fields;

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
    void severalFieldsInjectedWell() {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AComponent aComponent = context.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);
        BComponent bComponent = context.getComponent("bComponent");
        Assertions.assertNotNull(bComponent);
        CComponent cComponent = context.getComponent("cComponent");
        Assertions.assertNotNull(cComponent);
        DComponent dComponent = context.getComponent("dComponent");
        Assertions.assertNotNull(dComponent);


        Assertions.assertEquals(bComponent, aComponent.bComponent);
        Assertions.assertEquals(cComponent, aComponent.cComponent);
        Assertions.assertEquals(dComponent, aComponent.dComponent);
    }
}