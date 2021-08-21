package xyz.tiny.injector.component_definition;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
public class AComponent {
    @Inject
    BComponent bComponent;
    String name = "cat";
}
