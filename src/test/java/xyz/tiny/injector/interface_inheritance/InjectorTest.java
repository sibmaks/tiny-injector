package xyz.tiny.injector.interface_inheritance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.Injector;
import xyz.tiny.injector.context.IContext;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    /**
     * If class implement interface, what marked as a component then class will be component too
     * @throws Exception
     */
    @Test
    void interfaceInheritance() throws Exception {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        BaseInterface baseInterface = context.getComponent("interface");
        InterfaceImpl anInterface = context.getComponent("interface");
        IIComponent iIComponent = context.getComponent("iIComponent");
        Assertions.assertNotNull(baseInterface);
        Assertions.assertNotNull(anInterface);
        Assertions.assertNotNull(iIComponent);
        Assertions.assertEquals(iIComponent, baseInterface.getValue());
        Assertions.assertEquals(anInterface, baseInterface);
    }
}