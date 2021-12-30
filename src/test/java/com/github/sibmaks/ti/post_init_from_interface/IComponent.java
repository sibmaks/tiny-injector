package com.github.sibmaks.ti.post_init_from_interface;

import com.github.sibmaks.ti.annotation.PostInitialization;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
public interface IComponent {
    @PostInitialization
    void init();
}
