package com.github.sibmaks.ti.field_injection_named_changed;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
class AComponent {
    @Inject
    @Named(value = "cComponent")
    public BComponent bComponent;
}
