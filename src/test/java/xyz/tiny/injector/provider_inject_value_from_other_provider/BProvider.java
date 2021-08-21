package xyz.tiny.injector.provider_inject_value_from_other_provider;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Provider;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class BProvider implements Provider<BComponent> {

    @Override
    public BComponent get() {
        return new BComponent("value");
    }
}