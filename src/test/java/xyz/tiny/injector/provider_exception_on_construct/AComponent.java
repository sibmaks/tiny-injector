package xyz.tiny.injector.provider_exception_on_construct;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 20-08-2021
 */
@Component
public class AComponent {
    @Inject
    BComponent bComponent;
}
