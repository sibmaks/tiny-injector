package xyz.tiny.injector.enum_cant_be_component;

import xyz.tiny.injector.annotation.Component;

/**
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
@Component
public enum EnumComponent {
    VALUE_1, VALUE_2;

    EnumComponent() {

    }
}
