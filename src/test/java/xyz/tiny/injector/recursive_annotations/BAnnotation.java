package xyz.tiny.injector.recursive_annotations;

import xyz.tiny.injector.annotation.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
@Target(TYPE)
@Retention(RUNTIME)
@AAnnotation
@Component
public @interface BAnnotation {
}
