package com.github.sibmaks.ti.method_injection_several_named_params;

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
    void methodInjectionSupportSeveralNamedParam() throws Throwable {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AComponent aComponent = context.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);
        BComponent bComponent = context.getComponent("test1");
        Assertions.assertNotNull(bComponent);
        CComponent cComponent = context.getComponent("test2");
        Assertions.assertNotNull(cComponent);

        Assertions.assertEquals(bComponent, aComponent.bComponent);
        Assertions.assertEquals(cComponent, aComponent.cComponent);
    }
}