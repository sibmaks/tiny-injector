package xyz.tiny.injector.context_listener_cant_throw_exception_in_constructor;

import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.context.listener.IContextListener;

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
