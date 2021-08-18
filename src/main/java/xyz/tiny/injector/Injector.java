package xyz.tiny.injector;

import lombok.extern.slf4j.Slf4j;
import xyz.tiny.injector.context.IContext;
import xyz.tiny.injector.context.IMutableContext;
import xyz.tiny.injector.utils.ReflectionUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

    public static IContext buildInjections(ClassLoader classLoader, Set<String> scanPackages) throws Exception {
        log.debug("Search components");
        Map<String, Class<? super Object>> globalComponents = findComponents(classLoader, scanPackages);
        log.debug("Components found");

        log.debug("Build context");
        IContext context = buildContext(globalComponents);
        log.debug("Context built");

        log.debug("Start injection");
        doInjections(globalComponents, context);
        log.debug("Injection finished");
        return context;
    }

    private static void doInjections(Map<String, Class<? super Object>> globalComponents, IContext context)
            throws IllegalAccessException, InvocationTargetException {
        for (Map.Entry<String, Class<? super Object>> entry : globalComponents.entrySet()) {
            ComponentDefinition<?> sourceComponent = context.getComponentDefinition(entry.getKey());
            doFieldInjection(context, entry, sourceComponent);
            doMethodInjection(context, entry, sourceComponent);
        }
    }

    private static void doMethodInjection(IContext context, Map.Entry<String, Class<? super Object>> entry,
                                          ComponentDefinition<?> sourceComponent)
            throws InvocationTargetException, IllegalAccessException {
        Set<Method> methods = ReflectionUtils.getMethods(entry.getValue());
        for (Method method : methods) {
            if(!method.isAnnotationPresent(Inject.class)) {
                continue;
            }
            if(method.getParameterCount() != 1) {
                throw new IllegalStateException(String.format("Injected method should have only 1 parameter: %s",
                        method.getName()));
            }
            Named named = method.getAnnotation(Named.class);
            if(named == null) {
                throw new IllegalStateException(
                        String.format("In case of method injection @Named should be used: %s",
                                method.getName()));
            }
            if(named.value() == null || "".equals(named.value())) {
                throw new IllegalStateException(
                        String.format("@Named should has value in case of method injection: %s",
                                method.getName()));
            }
            Object component = context.getComponent(named.value());
            if(component == null) {
                throw new IllegalStateException(
                        String.format("Component with name '%s' not found. Requested in component: %s",
                                named.value(), entry.getKey()));
            }
            method.setAccessible(true);
            method.invoke(sourceComponent.getComponentInstance(), component);
            method.setAccessible(false);
        }
    }

    private static void doFieldInjection(IContext context, Map.Entry<String, Class<? super Object>> entry,
                                         ComponentDefinition<?> sourceComponent)
            throws IllegalAccessException {
        Set<Field> fields = ReflectionUtils.getFields(entry.getValue());
        for (Field field : fields) {
            if(!field.isAnnotationPresent(Inject.class)) {
                continue;
            }
            Named named = field.getAnnotation(Named.class);
            String componentName = named == null || named.value() == null || "".equals(named.value()) ?
                    field.getName() : named.value();
            Object component = context.getComponent(componentName);
            if(component == null) {
                throw new IllegalStateException(
                        String.format("Component with name '%s' not found. Requested in component: %s",
                                componentName, entry.getKey()));
            }
            field.setAccessible(true);
            field.set(sourceComponent.getComponentInstance(), component);
            field.setAccessible(false);
        }
    }

    private static IMutableContext buildContext(Map<String, Class<? super Object>> globalComponents) throws InstantiationException, IllegalAccessException {
        IMutableContext context = new HashMapContext();
        try {
            for (Map.Entry<String, Class<? super Object>> entry : globalComponents.entrySet()) {
                Object instance = entry.getValue().newInstance();
                if (!context.add(entry.getKey(), entry.getValue(), instance)) {
                    throw new IllegalStateException(String.format("Duplicates component name: %s", entry.getKey()));
                }
            }
        } catch (Throwable throwable) {
            context.clear();
            throw throwable;
        }
        return context;
    }

    private static Map<String, Class<? super Object>> findComponents(ClassLoader classLoader, Set<String> scanPackages)
            throws IOException {
        Map<String, Class<? super Object>> globalComponents = new ConcurrentHashMap<>();
        for (String scanPackage : scanPackages) {
            Map<String, Class<?>> components = ReflectionUtils.findComponents(classLoader, scanPackage);
            for (Map.Entry<String, Class<?>> entry : components.entrySet()) {
                if (entry.getValue().isInterface()) {
                    throw new IllegalStateException(String.format("Interface %s hasn't implementation", entry.getValue().getName()));
                }
                if (globalComponents.putIfAbsent(entry.getKey(), (Class<? super Object>) entry.getValue()) != null) {
                    throw new IllegalStateException(
                            String.format("Duplicates component name: %s in different packages (%s, %s)",
                                    entry.getKey(), entry.getValue().getPackage().getName(),
                                    globalComponents.get(entry.getKey()).getPackage().getName()));
                }
            }
        }
        return globalComponents;
    }

    public static IContext buildInjections(Set<String> scanPackages) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return buildInjections(classLoader, scanPackages);
    }

    public static IContext buildInjections(String ... scanPackages) throws Exception {
        Set<String> packages = new HashSet<>(Arrays.asList(scanPackages));
        return buildInjections(packages);
    }
}
