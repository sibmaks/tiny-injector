package com.github.sibmaks.ti.field_inject_not_exists_component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.Injector;
import com.github.sibmaks.ti.exception.FieldInjectionException;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    /**
     * IllegalStateException expected in case of injecting not existing component
     */
    @Test
    void fieldInjectedComponentNotExists() {
        Assertions.assertThrows(FieldInjectionException.class, () -> Injector.buildInjections(InjectorTest.class.getPackage().getName()));
    }
}