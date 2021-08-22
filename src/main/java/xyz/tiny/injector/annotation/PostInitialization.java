package xyz.tiny.injector.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotations for marking method as 2nd level constructor.
 * Method should have parameters.
 * Returning value are ignored.
 *
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface PostInitialization {
}
