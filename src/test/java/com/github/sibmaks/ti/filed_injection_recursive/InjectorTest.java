package com.github.sibmaks.ti.filed_injection_recursive;

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
    void recursiveInjections() throws Throwable {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        ARecursiveComponent aComponent = context.getComponent("aComponent");
        BRecursiveComponent bComponent = context.getComponent("bComponent");
        Assertions.assertNotNull(aComponent);
        Assertions.assertNotNull(bComponent);
        Assertions.assertEquals(aComponent, bComponent.aComponent);
        Assertions.assertEquals(bComponent, aComponent.bComponent);
    }
}