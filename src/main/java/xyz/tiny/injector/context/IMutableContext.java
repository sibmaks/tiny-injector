package xyz.tiny.injector.context;

/**
 * Mutable context for internal using
 *
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
public interface IMutableContext extends IContext {

    <T> boolean add(String name, Class<T> clazz, T component);

    void clear();
}
