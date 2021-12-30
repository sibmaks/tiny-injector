package com.github.sibmaks.ti.filed_injection_recursive;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component("aComponent")
class ARecursiveComponent {
    @Inject
    public BRecursiveComponent bComponent;
}
