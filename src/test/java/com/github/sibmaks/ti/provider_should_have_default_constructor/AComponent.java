package com.github.sibmaks.ti.provider_should_have_default_constructor;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 20-08-2021
 */
@Component
public class AComponent {
    @Inject
    BComponent bComponent;
}
