package xyz.tiny.injector.jar;

import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.jar.components.AComponent;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class DComponent {
    @Inject
    AComponent aComponent;
}
