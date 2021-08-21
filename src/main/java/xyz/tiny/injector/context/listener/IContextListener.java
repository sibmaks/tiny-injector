package xyz.tiny.injector.context.listener;

import xyz.tiny.injector.ComponentDefinition;
import xyz.tiny.injector.context.IContext;
import xyz.tiny.injector.context.IMutableContext;

/**
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
public interface IContextListener {
    /**
     * Called once when context just created not filled by injector
     * Context can add some component definitions into context
     * @param mutableContext modifiable context
     */
    default void onCreated(IMutableContext mutableContext) throws Exception {

    }

    /**
     * Called once when context fully initialized by Injector and all listeners.
     * Context can't be modified since this time
     * @param context read-only context
     */
    default void onInitialized(IContext context) throws Exception {

    }

    /**
     * Called each time then component definition is changed
     *
     * @param context modifiable context
     */
    default void onUpdated(ComponentDefinition<?> componentDefinition, IMutableContext context) throws Exception {

    }

    /**
     * Called each time when component definition added to context
     * @param componentDefinition not null component definition
     * @param context modifiable context
     */
    default void onAddComponentDefinition(ComponentDefinition<?> componentDefinition, IMutableContext context) throws Exception {

    }
}
