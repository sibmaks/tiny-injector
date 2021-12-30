package com.github.sibmaks.ti.context.base;

import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
public class AProvider implements Provider<String> {

    @Override
    @Named("providedComponent")
    public String get() {
        return "text";
    }
}
