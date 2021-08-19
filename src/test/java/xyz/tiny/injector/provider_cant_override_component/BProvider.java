package xyz.tiny.injector.provider_cant_override_component;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author drobyshev-ma
 * Created at 20-08-2021
 */
@Component
public class BProvider implements Provider<BComponent> {
    @Override
    @Named
    public BComponent get() {
        return new BComponent();
    }
}
