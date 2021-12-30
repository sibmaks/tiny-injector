package com.github.sibmaks.ti.provider_exception_on_construct;

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
