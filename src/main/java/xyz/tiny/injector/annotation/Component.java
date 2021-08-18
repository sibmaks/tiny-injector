package xyz.tiny.injector.annotation;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotations should be used to mark class as component, for turing on injection feature
 *
 * @author drobyshev-ma
 * Created at 18-08-2021
 */
@Target(TYPE)
@Retention(RUNTIME)
@Qualifier
public @interface Component {
    String value() default "";
}