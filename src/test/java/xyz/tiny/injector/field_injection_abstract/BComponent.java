package xyz.tiny.injector.field_injection_abstract;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
class BComponent {
    @Inject
    public AbstractComponent aComponent;
}
