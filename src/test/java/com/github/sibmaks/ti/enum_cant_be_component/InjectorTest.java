package com.github.sibmaks.ti.enum_cant_be_component;

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
    void enumAsComponent() throws Throwable {
        IContext iContext = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        EnumComponent enumComponent = iContext.getComponent("enumComponent");
        Assertions.assertNull(enumComponent);
    }
}