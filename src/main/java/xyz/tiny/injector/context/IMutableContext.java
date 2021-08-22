package xyz.tiny.injector.context;

import xyz.tiny.injector.reflection.ClassInfo;

/**
 * Mutable context for internal using
 *
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
public interface IMutableContext extends IContext {

    /**
     * Add component into context
     * @param name component name
     * @param clazz component class
     * @param component component instance
     * @param <T> component type
     * @throws Exception can throw exception on insert, for example insert duplicate
     */
    <T> void add(String name, ClassInfo<T> clazz, T component) throws Exception;

    /**
     * Update component instance.
     *
     * Trigger for {@code xyz.tiny.injector.context.listener.IContextListener#onUpdated} with {@code xyz.tiny.injector.context.UpdateType == INSTANCE_CHANGED}.
     * If new instance same as current nothing should be happened.
     * @param name component name
     * @param newInstance new component instance
     * @param <T> component type
     * @throws Exception can be thrown if component not found or type is mismatch, also some of listener can throw exception
     */
    <T> void update(String name, T newInstance) throws Exception;

    /**
     * Add mark to component definition, if mark already exists do nothing.
     *
     * Trigger for {@code xyz.tiny.injector.context.listener.IContextListener#onUpdated} with {@code xyz.tiny.injector.context.UpdateType == MARKED}.
     *
     * @param name component name
     * @param mark mark
     * @param <T> component type
     * @throws Exception can be thrown if component not found or some of the listeners throw an exception
     */
    <T> void addMark(String name, Object mark) throws Exception;

    /**
     * Clear context state
     * Can be used on application shutdown on if context more not needed
     */
    void clear();
}
