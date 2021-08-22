package xyz.tiny.injector;

/**
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
public interface IComponentDefinitionObserver {
    void onMarkAdded(ComponentDefinition<?> componentDefinition) throws Exception;
}
