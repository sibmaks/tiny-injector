package com.github.sibmaks.ti.post_init_simple;

import com.github.sibmaks.ti.annotation.Component;
import com.github.sibmaks.ti.annotation.PostInitialization;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class AComponent {
    @Inject
    BComponent bComponent;
    String content;

    @PostInitialization
    private void init() {
        content = bComponent.content;
    }
}
