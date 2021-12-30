package com.github.sibmaks.ti.post_init_should_have_not_parameters;

import com.github.sibmaks.ti.annotation.Component;
import com.github.sibmaks.ti.annotation.PostInitialization;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class AComponent {

    @PostInitialization
    private void init(String param) {

    }
}
