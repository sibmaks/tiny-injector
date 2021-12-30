package com.github.sibmaks.ti.provide_named_default_component;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author drobyshev-ma
 * Created at 20-08-2021
 */
@Component
public class BProvider implements Provider<BComponent> {
    @Override
    @Named
    public BComponent get() {
        return new BComponent();
    }
}
