package com.github.sibmaks.ti.provider_exception_on_get;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Provider;

/**
 * @author drobyshev-ma
 * Created at 20-08-2021
 */
@Component
public class BProvider implements Provider<BComponent> {
    @Override
    public BComponent get() {
        throw new RuntimeException();
    }
}
