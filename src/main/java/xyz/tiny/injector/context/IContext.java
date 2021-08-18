package xyz.tiny.injector.context;

import xyz.tiny.injector.ComponentDefinition;

/**
 * Common injection context, contains all components with them definitions
 *
 * @author drobyshev-ma
 * Created at 18-08-2021
 */
public interface IContext {
    <T> T getComponent(String componentName);

    <T> ComponentDefinition<T> getComponentDefinition(String componentName);
}
