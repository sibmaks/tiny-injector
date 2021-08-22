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
    public void setValue(@Named("bComponent1") BComponent bComponent) {
        bComponent1 = bComponent;
    }

    @Inject
    void someValue2(@Named("bComponent2") BComponent bComponent) {
        bComponent2 = bComponent;
    }
}
