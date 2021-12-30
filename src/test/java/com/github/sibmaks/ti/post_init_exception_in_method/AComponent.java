package com.github.sibmaks.ti.post_init_exception_in_method;

import com.github.sibmaks.ti.annotation.Component;
import com.github.sibmaks.ti.annotation.PostInitialization;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class AComponent {

    @PostInitialization
    private void init() {
        throw new RuntimeException();
    }
}
