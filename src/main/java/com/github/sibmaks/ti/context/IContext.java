package com.github.sibmaks.ti.context;

import com.github.sibmaks.ti.ComponentDefinition;

/**
 * Common injection context, contains all components with them definitions
 *
 * @author drobyshev-ma
 * Created at 18-08-2021
 */
public interface IContext {
    /**
     * Get component instance by name
     * @param componentName component name
     * @param <T> component type
     * @return component instance
     */
    <T> T getComponent(String componentName);

    /**
     * Get component definition by name
     * @param componentName component name
     * @param <T> component type
     * @return component definition
     */
    <T> ComponentDefinition<T> getComponentDefinition(String componentName);
}
