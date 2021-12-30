package com.github.sibmaks.ti.method_injection_several_named_params;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class AComponent {
    BComponent bComponent;
    CComponent cComponent;

    @Inject
    public void doInject(@Named("test1") BComponent bComponent, @Named("test2") CComponent cComponent) {
        this.bComponent = bComponent;
        this.cComponent = cComponent;
    }
}
