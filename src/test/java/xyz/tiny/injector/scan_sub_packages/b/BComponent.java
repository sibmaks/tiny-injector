package xyz.tiny.injector.scan_sub_packages.b;

import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.scan_sub_packages.a.AComponent;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 20-08-2021
 */
@Component
public class BComponent {
    @Inject
    public AComponent aComponent;
}
