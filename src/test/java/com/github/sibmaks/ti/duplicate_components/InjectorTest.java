package com.github.sibmaks.ti.duplicate_components;

import com.github.sibmaks.ti.Injector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    /**
     * IllegalStateException expected in case of two components have the same name
     */
    @Test
    void duplicateComponentsInjection() {
        String name = InjectorTest.class.getPackage().getName();
        Assertions.assertThrows(IllegalStateException.class, () -> Injector.buildInjections(name));
    }
}