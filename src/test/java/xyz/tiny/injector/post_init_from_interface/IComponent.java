package xyz.tiny.injector.post_init_from_interface;

import xyz.tiny.injector.annotation.PostInitialization;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
public interface IComponent {
    @PostInitialization
    void init();
}
