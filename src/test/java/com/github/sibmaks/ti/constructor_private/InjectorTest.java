package com.github.sibmaks.ti.constructor_private;

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
    void privateConstructor() throws Throwable {
        IContext context = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        Assertions.assertNotNull(context.getComponent("aComponent"));
    }
}