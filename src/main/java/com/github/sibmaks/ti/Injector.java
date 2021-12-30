package com.github.sibmaks.ti;

import com.github.sibmaks.ti.context.IContext;
import com.github.sibmaks.ti.context.IMutableContext;
import com.github.sibmaks.ti.context.base.FieldInjector;
import com.github.sibmaks.ti.exception.InitializationException;
import com.github.sibmaks.ti.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import com.github.sibmaks.ti.context.listener.IContextListener;
import com.github.sibmaks.ti.reflection.ClassInfo;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class provided injections feature
 *
 * @author drobyshev-ma
 * Created at 18-08-2021
 */
@Slf4j
public class Injector {

    private Injector() {

    }

    public static IContext buildInjections(ClassLoader classLoader, Set<String> scanPackages) {
        scanPackages = new HashSet<>(scanPackages);
        scanPackages.add(FieldInjector.class.getPackage().getName());

        try {
            log.debug("Search components");
            Map<String, ClassInfo<? super Object>> globalComponents = findComponents(classLoader, scanPackages);
            log.debug("Components found");

            log.debug("Search context listeners");
            List<IContextListener> contextListeners = findContextListeners(globalComponents);
            log.debug("Listeners found");

            log.debug("Build context");
            IContext context = buildContext(globalComponents, contextListeners);
            log.debug("Context built");

            return context;
        } catch (InvocationTargetException e) {
            throw new InitializationException(e.getCause());
        }  catch (InitializationException e) {
            throw e;
        } catch (Exception e) {
            throw new InitializationException(e);
        }
    }

    /**
     * Method is responsible for search context listeners (and removing them from global components register)
     * @param globalComponents
     * @return
     */
    private static List<IContextListener> findContextListeners(Map<String, ClassInfo<? super Object>> globalComponents)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<String> listenerNames = new ArrayList<>();
        List<IContextListener> listeners = new ArrayList<>();
        for (Map.Entry<String, ClassInfo<? super Object>> it : globalComponents.entrySet()) {
            if (IContextListener.class.isAssignableFrom(it.getValue().get())) {
                Constructor<? super Object> constructor = it.getValue().get().getDeclaredConstructor();
                constructor.setAccessible(true);
                Object instance = constructor.newInstance();
                constructor.setAccessible(false);
                listeners.add((IContextListener) instance);
                listenerNames.add(it.getKey());
            }
        }
        for (String name : listenerNames) {
            globalComponents.remove(name);
        }
        return listeners;
    }

    private static IMutableContext buildContext(Map<String, ClassInfo<? super Object>> globalComponents,
                                                List<IContextListener> contextListeners)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        SimpleContext context = new SimpleContext(contextListeners);
        try {
            initComponents(globalComponents, context);
        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread(context::clear));
        }
        context.onInitializationFinished();
        return context;
    }

    private static void initComponents(Map<String, ClassInfo<? super Object>> globalComponents, IMutableContext context)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Map.Entry<String, ClassInfo<? super Object>> entry : globalComponents.entrySet()) {
            Constructor<? super Object> constructor = entry.getValue().get().getDeclaredConstructor();
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();
            constructor.setAccessible(false);
            context.add(entry.getKey(), entry.getValue(), instance);
        }
    }

    private static Map<String, ClassInfo<? super Object>> findComponents(ClassLoader classLoader, Set<String> scanPackages)
            throws IOException {
        Map<String, ClassInfo<? super Object>> globalComponents = new ConcurrentHashMap<>();
        for (String scanPackage : scanPackages) {
            Map<String, ClassInfo<? super Object>> components = ReflectionUtils.findComponents(classLoader, scanPackage);
            for (Map.Entry<String, ClassInfo<? super Object>> entry : components.entrySet()) {
                ClassInfo<? super Object> classInfo = entry.getValue();
                if (globalComponents.putIfAbsent(entry.getKey(), classInfo) != null) {
                    throw new IllegalStateException(
                            String.format("Duplicates component name: %s in different packages (%s, %s)",
                                    entry.getKey(), classInfo.getPackageName(),
                                    globalComponents.get(entry.getKey()).getPackageName()));
                }
            }
        }
        return globalComponents;
    }

    public static IContext buildInjections(Set<String> scanPackages) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return buildInjections(classLoader, scanPackages);
    }

    public static IContext buildInjections(String ... scanPackages) {
        Set<String> packages = new HashSet<>(Arrays.asList(scanPackages));
        return buildInjections(packages);
    }
}
