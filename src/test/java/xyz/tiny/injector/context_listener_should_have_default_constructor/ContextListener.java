package xyz.tiny.injector.context_listener_should_have_default_constructor;

import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.context.listener.IContextListener;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class ContextListener implements IContextListener {
    public ContextListener(String param) {

    }
}
