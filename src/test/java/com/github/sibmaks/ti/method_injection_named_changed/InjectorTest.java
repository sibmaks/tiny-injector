package com.github.sibmaks.ti.method_injection_named_changed;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;
import com.github.sibmaks.ti.context.IContext;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    /**
     * Method will be injected by type name if @Named not presented
     */
    @Test
    void injectWithFilledNamedAnnotation() {
        IContext iContext = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AComponent aComponent = iContext.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);
        Assertions.assertEquals(aComponent, aComponent.aComponent);
    }
}