package com.github.sibmaks.ti.method_injection_named_default;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
public class AComponent {
    AComponent aComponent;

    @Inject
    public void doInject(@Named AComponent component) {
        this.aComponent = component;
    }
}
