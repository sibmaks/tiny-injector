package com.github.sibmaks.ti.reflection;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class AComponent {
    @Inject
    BComponent bComponent;
}
