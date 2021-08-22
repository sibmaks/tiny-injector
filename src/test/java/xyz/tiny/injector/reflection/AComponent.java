package xyz.tiny.injector.reflection;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class AComponent {
    @Inject
    BComponent bComponent;
}
