package com.github.sibmaks.ti.inheritance_annotation;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
abstract class AbstractComponent {
    @Inject
    AComponent aComponent;
}
