package xyz.tiny.injector.inheritance_annotation;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
abstract class AbstractComponent {
    @Inject
    AComponent aComponent;
}
