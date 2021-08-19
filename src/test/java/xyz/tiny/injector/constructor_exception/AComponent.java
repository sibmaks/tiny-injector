package xyz.tiny.injector.constructor_exception;

import xyz.tiny.injector.annotation.Component;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
public class AComponent {
    public AComponent() {
        throw new RuntimeException();
    }
}
