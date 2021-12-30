package com.github.sibmaks.ti.component_can_be_proxed;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
public class AComponent {
    @Inject
    AComponent aComponent;

    public void method() {

    }
}
