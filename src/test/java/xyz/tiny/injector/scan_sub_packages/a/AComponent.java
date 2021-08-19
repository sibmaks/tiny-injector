package xyz.tiny.injector.scan_sub_packages.a;

import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.scan_sub_packages.b.BComponent;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
public class AComponent {
    @Inject
    public BComponent bComponent;
}
