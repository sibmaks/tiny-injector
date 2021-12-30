package com.github.sibmaks.ti.field_injection_named_default;

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
    void defaultComponentNameInjection() {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AComponent aComponent = context.getComponent("aComponent");
        BComponent bComponent = context.getComponent("bComponent");
        Assertions.assertNotNull(aComponent);
        Assertions.assertNotNull(bComponent);
        Assertions.assertEquals(bComponent, aComponent.bComponent);
    }
}