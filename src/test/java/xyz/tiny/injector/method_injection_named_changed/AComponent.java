package xyz.tiny.injector.method_injection_named_changed;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
public class AComponent {
    AComponent aComponent;

    @Inject
    public void doInject(AComponent component) {
        this.aComponent = component;
    }
}
