package xyz.tiny.injector.field_injection_in_provider;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
@Component
public class AProvider implements Provider<AComponent> {
    @Inject
    private BComponent bComponent;

    @Override
    public AComponent get() {
        AComponent aComponent = new AComponent();
        aComponent.bComponent = bComponent;
        return aComponent;
    }
}
