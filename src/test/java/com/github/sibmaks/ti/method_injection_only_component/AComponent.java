package com.github.sibmaks.ti.method_injection_only_component;

import com.github.sibmaks.ti.annotation.Component;
import com.github.sibmaks.ti.duplicate_components.BComponent;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
public class AComponent {
    @Inject
    public void doInject(@Named("bComponent") BComponent bComponent) {

    }
}
