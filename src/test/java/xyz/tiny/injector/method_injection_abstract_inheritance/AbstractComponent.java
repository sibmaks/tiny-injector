package xyz.tiny.injector.method_injection_abstract_inheritance;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
abstract class AbstractComponent {
    BComponent bComponent3;

    @Inject
    abstract void setValue(BComponent bComponent);

    @Inject
    @Named("bComponent3")
    private void someValue2(BComponent bComponent) {
        this.bComponent3 = bComponent;
    }
}