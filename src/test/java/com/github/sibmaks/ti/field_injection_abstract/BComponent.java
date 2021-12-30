package com.github.sibmaks.ti.field_injection_abstract;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
class BComponent {
    @Inject
    public AbstractComponent aComponent;
}
