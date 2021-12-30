package com.github.sibmaks.ti.provider_inject_value_from_other_provider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;
import com.github.sibmaks.ti.context.IContext;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
class InjectorTest {

    @Test
    void providerCanInjectValueFromOtherProvider() throws Throwable {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        BComponent bComponent = context.getComponent("bComponent");
        Assertions.assertNotNull(bComponent);
        String string = context.getComponent("string");
        Assertions.assertNotNull(string);

        Assertions.assertEquals(bComponent.value, string);
    }
}