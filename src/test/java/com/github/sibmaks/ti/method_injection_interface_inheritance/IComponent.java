package com.github.sibmaks.ti.method_injection_interface_inheritance;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
interface IComponent {
    @Inject
    void setValue(BComponent bComponent);
}
