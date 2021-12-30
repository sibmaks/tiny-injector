package com.github.sibmaks.ti.provider_exception_on_construct;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Provider;

/**
 * @author drobyshev-ma
 * Created at 20-08-2021
 */
@Component
public class BProvider implements Provider<BComponent> {
    public BProvider() {
        throw new RuntimeException();
    }

    @Override
    public BComponent get() {
        return new BComponent();
    }
}
