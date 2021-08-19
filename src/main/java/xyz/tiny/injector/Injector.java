package xyz.tiny.injector;

import lombok.extern.slf4j.Slf4j;
import xyz.tiny.injector.context.IContext;
import xyz.tiny.injector.context.IMutableContext;
import xyz.tiny.injector.exception.FieldInjectionException;
import xyz.tiny.injector.exception.MethodInjectionException;
import xyz.tiny.injector.utils.ReflectionUtils;
import xyz.tiny.injector.utils.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
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
                                          ComponentDefinition<?> sourceComponent) {
        Set<Method> methods = ReflectionUtils.getMethods(entry.getValue());
        for (Method method : methods) {
            List<Annotation> annotations = ReflectionUtils.getMethodAnnotations(entry.getValue(), method);
            if(annotations.stream().noneMatch(it -> it.annotationType() == Inject.class)) {
                continue;
            }
            if(method.getParameterCount() != 1) {
                throw new IllegalStateException(String.format("Injected method should have only 1 parameter: %s#%s",
                        entry.getValue().getName(), method.getName()));
            }
            Named named = (Named) annotations.stream()
                    .filter(it -> it.annotationType() == Named.class)
                    .findFirst()
                    .orElse(null);
            String name = named == null ? null : named.value();
            if(name == null || "".equals(name)) {
                Class<?> parameterType = method.getParameterTypes()[0];
                name = StringUtils.toLowerCase1st(parameterType.getSimpleName());
            }
            Object component = context.getComponent(name);
            if(component == null) {
                throw new IllegalStateException(
                        String.format("Component with name '%s' not found. Requested in component: %s",
                                name, entry.getKey()));
            }
            method.setAccessible(true);
            try {
                method.invoke(sourceComponent.getComponentInstance(), component);
            } catch (Throwable throwable) {
                throw new MethodInjectionException(throwable);
            }
            method.setAccessible(false);
        }
    }

    private static void doFieldInjection(IContext context, Map.Entry<String, Class<? super Object>> entry,
                                         ComponentDefinition<?> sourceComponent)
            throws IllegalAccessException {
        Set<Field> fields = ReflectionUtils.getFields(entry.getValue());
        for (Field field : fields) {
            List<Annotation> annotations = ReflectionUtils.getFieldAnnotations(entry.getValue(), field);
            if(annotations.stream().noneMatch(it -> it.annotationType() == Inject.class)) {
                continue;
            }
            if(Modifier.isFinal(field.getModifiers())) {
                throw new FieldInjectionException(String.format("Can't inject final field: %s.%s",
                        entry.getValue().getName(), field.getName()));
            }
            Named named = (Named) annotations.stream()
                    .filter(it -> it.annotationType() == Named.class)
                    .findFirst()
                    .orElse(null);
            String componentName = named == null || "".equals(named.value()) ?
                    field.getName() : named.value();
            Object component = context.getComponent(componentName);
            if(component == null) {
                throw new FieldInjectionException(
                        String.format("Component with name '%s' not found. Requested in component: %s",
                                componentName, entry.getKey()));
            }
            field.setAccessible(true);
            field.set(sourceComponent.getComponentInstance(), component);
            field.setAccessible(false);
        }
    }

    private static IMutableContext buildContext(Map<String, Class<? super Object>> globalComponents) throws Exception {
        IMutableContext context = new HashMapContext();
        try {
            for (Map.Entry<String, Class<? super Object>> entry : globalComponents.entrySet()) {
                Constructor<? super Object> constructor = entry.getValue().getDeclaredConstructor();
                constructor.setAccessible(true);
                Object instance = constructor.newInstance();
                constructor.setAccessible(false);
                if (!context.add(entry.getKey(), entry.getValue(), instance)) {
                    throw new IllegalStateException(String.format("Duplicates component name: %s", entry.getKey()));
                }
            }
        } catch (Throwable throwable) {
            context.clear();
            throw throwable;
        }
        Runtime.getRuntime().addShutdownHook(new Thread(context::clear));
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
                if (Modifier.isAbstract(entry.getValue().getModifiers())) {
                    throw new IllegalStateException(String.format("Abstract class %s hasn't implementation", entry.getValue().getName()));
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
