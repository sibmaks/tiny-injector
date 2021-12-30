package com.github.sibmaks.ti.context_listener_cant_throw_exception_in_constructor;

import com.github.sibmaks.ti.annotation.Component;
import com.github.sibmaks.ti.context.listener.IContextListener;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class ContextListener implements IContextListener {
    public ContextListener() {
        throw new RuntimeException();
    }
}
