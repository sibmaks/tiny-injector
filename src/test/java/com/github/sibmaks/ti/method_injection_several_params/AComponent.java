package com.github.sibmaks.ti.method_injection_several_params;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class AComponent {
    BComponent bComponent;
    CComponent cComponent;

    @Inject
    public void doInject(BComponent bComponent, CComponent cComponent) {
        this.bComponent = bComponent;
        this.cComponent = cComponent;
    }
}
