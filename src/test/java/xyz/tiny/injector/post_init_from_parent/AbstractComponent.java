package xyz.tiny.injector.post_init_from_parent;

import xyz.tiny.injector.annotation.PostInitialization;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
public abstract class AbstractComponent {
    @PostInitialization
    abstract protected void init();
}
