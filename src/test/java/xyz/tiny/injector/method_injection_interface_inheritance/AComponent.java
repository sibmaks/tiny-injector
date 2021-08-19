package xyz.tiny.injector.method_injection_interface_inheritance;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
public class AComponent implements IComponent {
    BComponent bComponent1;
    BComponent bComponent2;

    @Override
    @Named("bComponent1")
    public void setValue(BComponent bComponent) {
        bComponent1 = bComponent;
    }

    @Inject
    @Named("bComponent2")
    void someValue2(BComponent bComponent) {
        bComponent2 = bComponent;
    }
}
