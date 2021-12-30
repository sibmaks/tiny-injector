package com.github.sibmaks.ti.field_injection_several_fields;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
class AComponent {
    @Inject
    public BComponent bComponent;
    @Inject
    public CComponent cComponent;
    @Inject
    public DComponent dComponent;
}
