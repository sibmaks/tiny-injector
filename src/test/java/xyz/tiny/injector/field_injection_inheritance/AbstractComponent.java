package xyz.tiny.injector.field_injection_inheritance;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
abstract class AbstractComponent {
    @Inject
    BComponent bComponent;
}
