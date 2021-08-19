package xyz.tiny.injector.method_injection_only_one_params;

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
    public void doInject(AComponent component1, AComponent component2) {

    }
}
