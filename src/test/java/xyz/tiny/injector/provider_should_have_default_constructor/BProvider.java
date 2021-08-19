package xyz.tiny.injector.provider_should_have_default_constructor;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Provider;

/**
 * @author drobyshev-ma
 * Created at 20-08-2021
 */
@Component
public class BProvider implements Provider<BComponent> {
    public BProvider(String param) {

    }

    @Override
    public BComponent get() {
        return new BComponent();
    }
}
