package xyz.tiny.injector.post_init_should_have_not_parameters;

import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.annotation.PostInitialization;

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
