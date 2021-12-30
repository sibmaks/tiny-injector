package com.github.sibmaks.ti.recursive_annotations;

import com.github.sibmaks.ti.annotation.Component;

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
