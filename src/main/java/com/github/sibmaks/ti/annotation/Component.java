package com.github.sibmaks.ti.annotation;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotations should be used to mark class as component, for turing on injection feature.
 * This annotation can be used for turing on implementation of {@link  com.github.sibmaks.ti.context.listener.IContextListener}
 * This annotation can be used in classes that annotated as Configuration.
 *
 *
 * @author drobyshev-ma
 * Created at 18-08-2021
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Qualifier
public @interface Component {
    String value() default "";
}