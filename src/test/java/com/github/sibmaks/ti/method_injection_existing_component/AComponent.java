package com.github.sibmaks.ti.method_injection_existing_component;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
public class AComponent {
    @Inject
    public void doInject(@Named("unknown") AComponent aComponent) {

    }
}
