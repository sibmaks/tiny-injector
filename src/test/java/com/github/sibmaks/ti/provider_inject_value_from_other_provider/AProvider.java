package com.github.sibmaks.ti.provider_inject_value_from_other_provider;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class AProvider implements Provider<String> {

    @Inject
    private BComponent bComponent;

    @Override
    public String get() {
        return bComponent.value;
    }
}
