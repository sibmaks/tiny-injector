package xyz.tiny.injector.filed_injection_recursive;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component("aComponent")
class ARecursiveComponent {
    @Inject
    public BRecursiveComponent bComponent;
}
