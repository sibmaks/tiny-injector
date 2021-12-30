package com.github.sibmaks.ti.post_init_from_parent;

import com.github.sibmaks.ti.annotation.PostInitialization;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
public abstract class AbstractComponent {
    @PostInitialization
    abstract protected void init();
}
