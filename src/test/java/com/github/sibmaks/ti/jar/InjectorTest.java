package com.github.sibmaks.ti.jar;

import com.github.sibmaks.ti.Injector;
import com.github.sibmaks.ti.context.IContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import other.test.pckg.ti.AComponent;
import other.test.pckg.ti.BComponent;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void componentsFromJarInjectedToo() {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName(), AComponent.class.getPackage().getName());
        AComponent aComponent = context.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);
        BComponent bComponent = context.getComponent("bComponent");
        Assertions.assertNotNull(bComponent);
        DComponent dComponent = context.getComponent("dComponent");
        Assertions.assertNotNull(dComponent);

        Assertions.assertEquals(bComponent, aComponent.bComponent);
        Assertions.assertEquals(aComponent, dComponent.aComponent);
    }
}