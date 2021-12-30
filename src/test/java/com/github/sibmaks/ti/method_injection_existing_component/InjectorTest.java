package com.github.sibmaks.ti.method_injection_existing_component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;
import com.github.sibmaks.ti.exception.MethodInjectionException;

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
        Assertions.assertThrows(MethodInjectionException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}