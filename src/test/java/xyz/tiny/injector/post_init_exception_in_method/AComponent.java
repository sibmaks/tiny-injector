package xyz.tiny.injector.post_init_exception_in_method;

import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.annotation.PostInitialization;

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
