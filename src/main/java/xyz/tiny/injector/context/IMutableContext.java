package xyz.tiny.injector.context;

import xyz.tiny.injector.reflection.ClassInfo;

/**
 * Mutable context for internal using
 *
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
public interface IMutableContext extends IContext {

    <T> void add(String name, ClassInfo<T> clazz, T component) throws Exception;

    <T> void update(String name, T newInstance) throws Exception;

    <T> void addMark(String name, Object mark) throws Exception;

    void clear();
}
