package xyz.tiny.injector.component_definition;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
public class BComponent {
    @Inject
    AComponent aComponent;
}
