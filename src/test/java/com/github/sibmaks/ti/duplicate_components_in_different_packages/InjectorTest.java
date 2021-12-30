package com.github.sibmaks.ti.duplicate_components_in_different_packages;

import com.github.sibmaks.ti.Injector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    /**
     * IllegalStateException expected in case of two components have the same name in different packages
     */
    @Test
    void duplicateComponentsInjection() {
        String name = InjectorTest.class.getPackage().getName();
        Assertions.assertThrows(IllegalStateException.class,
                () -> Injector.buildInjections(name + ".a", name + ".b"));
    }
}