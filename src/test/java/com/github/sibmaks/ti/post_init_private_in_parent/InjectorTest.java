package com.github.sibmaks.ti.post_init_private_in_parent;

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
    void postInitPrivateMethodCalledForParent() {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AComponent aComponent = context.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);
        
        Assertions.assertEquals(1, aComponent.atomicInteger.get());
        Assertions.assertEquals(1, aComponent.atomicInteger2.get());
    }
}