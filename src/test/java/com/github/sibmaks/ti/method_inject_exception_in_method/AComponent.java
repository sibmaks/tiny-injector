package com.github.sibmaks.ti.method_inject_exception_in_method;

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
    public void doInject(@Named("aComponent") AComponent component) {
        throw new RuntimeException();
    }
}
