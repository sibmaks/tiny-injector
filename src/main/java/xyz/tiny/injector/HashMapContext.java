package xyz.tiny.injector;

import xyz.tiny.injector.context.IMutableContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation of mutable context
 * Not thread safe
 *
 * @author drobyshev-ma
 * Created at 18-08-2021
 */
class HashMapContext implements IMutableContext {
    private final Map<String, ComponentDefinition<?>> components;

    HashMapContext() {
        this.components = new HashMap<>();
    }

    @Override
    public void add(String name, Class clazz, Object component) {
        components.putIfAbsent(name, new ComponentDefinition<>(name, clazz, component));
    }

    @Override
    public void clear() {
        components.clear();
    }

    @Override
    public <T> ComponentDefinition<T> getComponentDefinition(String componentName) {
        return (ComponentDefinition<T>) components.get(componentName);
    }

    @Override
    public <T> T getComponent(String componentName) {
        ComponentDefinition<?> definition = components.get(componentName);
        if(definition == null) {
            return null;
        }
        return (T) definition.getComponentInstance();
    }
}
