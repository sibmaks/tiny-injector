package com.github.sibmaks.ti.jar;

import com.github.sibmaks.ti.annotation.Component;
import other.test.pckg.ti.AComponent;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class DComponent {
    @Inject
    AComponent aComponent;
}
