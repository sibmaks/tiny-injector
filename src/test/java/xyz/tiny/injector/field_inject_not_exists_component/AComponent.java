package xyz.tiny.injector.field_inject_not_exists_component;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
@Singleton
public class AComponent {
    @Inject
    @Named("unknown")
    private AComponent aComponent;
}
