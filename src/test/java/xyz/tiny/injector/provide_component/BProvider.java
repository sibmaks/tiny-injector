package xyz.tiny.injector.provide_component;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Provider;

/**
 * @author drobyshev-ma
 * Created at 20-08-2021
 */
@Component
public class BProvider implements Provider<BComponent> {
    @Override
    public BComponent get() {
        return new BComponent();
    }
}
