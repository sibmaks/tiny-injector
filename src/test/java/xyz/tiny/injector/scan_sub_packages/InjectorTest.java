package xyz.tiny.injector.scan_sub_packages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.Injector;
import xyz.tiny.injector.context.IContext;
import xyz.tiny.injector.scan_sub_packages.a.AComponent;
import xyz.tiny.injector.scan_sub_packages.b.BComponent;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void scanSubPackages() throws Throwable {
        IContext iContext = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AComponent aComponent = iContext.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);

        BComponent bComponent = iContext.getComponent("bComponent");
        Assertions.assertNotNull(aComponent);

        Assertions.assertEquals(bComponent, aComponent.bComponent);
        Assertions.assertEquals(aComponent, bComponent.aComponent);
    }
}