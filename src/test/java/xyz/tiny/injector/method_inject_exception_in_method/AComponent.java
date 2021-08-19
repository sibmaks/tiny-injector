package xyz.tiny.injector.method_inject_exception_in_method;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
public class AComponent {
    @Inject
    @Named("aComponent")
    public void doInject(AComponent component) {
        throw new RuntimeException();
    }
}
