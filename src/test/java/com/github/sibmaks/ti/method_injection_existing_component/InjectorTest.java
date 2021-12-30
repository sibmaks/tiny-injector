package com.github.sibmaks.ti.method_injection_existing_component;

import com.github.sibmaks.ti.Injector;
import com.github.sibmaks.ti.exception.InitializationException;
import com.github.sibmaks.ti.exception.MethodInjectionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    /**
     * IllegalStateException expected in case of method injection when Named annotation contains invalid component name
     */
    @Test
    void namedAnnotationsShouldHaveValidName() {
        String name = InjectorTest.class.getPackage().getName();
        Assertions.assertThrows(MethodInjectionException.class, () -> Injector.buildInjections(name));
    }
}