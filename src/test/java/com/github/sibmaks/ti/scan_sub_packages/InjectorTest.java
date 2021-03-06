package com.github.sibmaks.ti.scan_sub_packages;

import com.github.sibmaks.ti.scan_sub_packages.a.AComponent;
import com.github.sibmaks.ti.scan_sub_packages.b.BComponent;
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
    void scanSubPackages() {
        IContext iContext = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AComponent aComponent = iContext.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);

        BComponent bComponent = iContext.getComponent("bComponent");
        Assertions.assertNotNull(aComponent);

        Assertions.assertEquals(bComponent, aComponent.bComponent);
        Assertions.assertEquals(aComponent, bComponent.aComponent);
    }
}