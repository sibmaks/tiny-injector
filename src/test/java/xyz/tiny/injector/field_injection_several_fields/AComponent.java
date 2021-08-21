package xyz.tiny.injector.field_injection_several_fields;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
class AComponent {
    @Inject
    public BComponent bComponent;
    @Inject
    public CComponent cComponent;
    @Inject
    public DComponent dComponent;
}
