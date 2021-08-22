package xyz.tiny.injector.post_init_simple;

import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.annotation.PostInitialization;

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
