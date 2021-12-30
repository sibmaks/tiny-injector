package com.github.sibmaks.ti;

import com.github.sibmaks.ti.context.IMutableContext;
import com.github.sibmaks.ti.context.UpdateType;
import com.github.sibmaks.ti.context.listener.IContextListener;
import com.github.sibmaks.ti.exception.ContextModificationException;
import com.github.sibmaks.ti.reflection.ClassInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simple implementation of mutable context
 * Not thread safe
 *
 * @author drobyshev-ma
 * Created at 18-08-2021
 */
class SimpleContext implements IMutableContext {
    private static final String CONTEXT_INITIALIZED_ERROR = "Context is already initialized";

    private final Map<String, ComponentDefinition<?>> components;
    private final List<IContextListener> contextListeners;
    private volatile boolean initialized;
    private final Lock stateLock = new ReentrantLock();

    SimpleContext(List<IContextListener> contextListeners) {
        this.components = new HashMap<>();
        this.contextListeners = Collections.unmodifiableList(contextListeners);

        for (IContextListener listener : contextListeners) {
            listener.onCreated(this);
        }
    }

    @Override
    public<T> void add(String name, ClassInfo<T> clazz, T component) throws ContextModificationException {
        stateLock.lock();
        try {
            if(initialized) {
                throw new IllegalStateException(CONTEXT_INITIALIZED_ERROR);
            }
            ComponentDefinition<? super Object> definition = new ComponentDefinition<>(name, (ClassInfo<Object>) clazz, component);
            ComponentDefinition<?> previous = components.putIfAbsent(name, definition);
            if (previous != null && previous != definition) {
                throw new IllegalStateException(String.format("Duplicate component with name %s: %s and %s", name,
                        previous.getComponentClass().get().getName(),
                        definition.getComponentClass().get().getName()));
            }
            for (IContextListener listener : contextListeners) {
                listener.onAddComponentDefinition(definition, this);
            }
        } finally {
            stateLock.unlock();
        }
    }

    @Override
    public<T> void update(String name, T newInstance) {
        stateLock.lock();
        try {
            if (initialized) {
                throw new IllegalStateException(CONTEXT_INITIALIZED_ERROR);
            }
            ComponentDefinition<T> componentDefinition = getComponentDefinition(name);
            if(componentDefinition.getComponentInstance() == newInstance) {
                return;
            }
            componentDefinition.setComponentInstance(newInstance);
            for (IContextListener listener : contextListeners) {
                listener.onUpdated(UpdateType.INSTANCE_CHANGED, componentDefinition, this);
            }
        } finally {
            stateLock.unlock();
        }
    }

    @Override
    public void addMark(String name, Object mark) throws ContextModificationException {
        stateLock.lock();
        try {
            if (initialized) {
                throw new IllegalStateException(CONTEXT_INITIALIZED_ERROR);
            }
            ComponentDefinition<?> componentDefinition = getComponentDefinition(name);
            if (componentDefinition.mark(mark)) {
                for (IContextListener listener : contextListeners) {
                    listener.onUpdated(UpdateType.MARKED, componentDefinition, this);
                }
            }
        } finally {
            stateLock.unlock();
        }
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

    public void onInitializationFinished() {
        for (IContextListener listener : contextListeners) {
            listener.onInitialized(this);
        }
        stateLock.lock();
        this.initialized = true;
        stateLock.unlock();
    }
}
