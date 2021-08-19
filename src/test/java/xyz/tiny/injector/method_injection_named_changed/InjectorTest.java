package xyz.tiny.injector.method_injection_named_changed;

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
     * Method will be injected by type name if @Named not presented
     */
    @Test
    void methodInjection() throws Exception {
        IContext iContext = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AComponent aComponent = iContext.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);
        Assertions.assertEquals(aComponent, aComponent.aComponent);
    }
}