package xyz.tiny.injector.context.base;

import xyz.tiny.injector.ComponentDefinition;
import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.context.IContext;
import xyz.tiny.injector.context.IMutableContext;
import xyz.tiny.injector.context.listener.IContextListener;
import xyz.tiny.injector.exception.MethodInjectionException;
import xyz.tiny.injector.reflection.AnnotationInfo;
import xyz.tiny.injector.reflection.ClassInfo;
import xyz.tiny.injector.reflection.MethodInfo;
import xyz.tiny.injector.utils.Pair;
import xyz.tiny.injector.utils.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
@Component
public class MethodInjector implements IContextListener {
    private Map<ComponentDefinition<?>, List<Pair<String, MethodInfo>>> pendingInjections;
    private Map<String, Set<ComponentDefinition<?>>> requiredComponents;

    @Override
    public void onCreated(IMutableContext mutableContext) {
        this.pendingInjections = new HashMap<>();
        this.requiredComponents = new HashMap<>();
    }

    @Override
    public void onInitialized(IContext context) {
        if(requiredComponents.isEmpty()) {
            return;
        }
        throw new MethodInjectionException(String.format("Components not found for method injections: %s",
                String.join(", ", requiredComponents.keySet())));
    }

    @Override
    public void onAddComponentDefinition(ComponentDefinition<?> componentDefinition, IMutableContext context)
            throws Exception {
        doComponentInjections(componentDefinition, context);
        doPendingInjections(componentDefinition, context);
    }

    private void doComponentInjections(ComponentDefinition<?> componentDefinition, IMutableContext context)
            throws Exception {
        ClassInfo<?> classInfo = componentDefinition.getComponentClass();
        boolean fullyInjected = true;
        for (MethodInfo methodInfo : classInfo.getMethodInfos()) {
            if(methodInfo.getAnnotationInfos().stream().noneMatch(it -> it.isInherited(Inject.class))) {
                continue;
            }
            Method method = methodInfo.getMethod();
            if(method.getParameterCount() != 1) {
                throw new MethodInjectionException(String.format("Injected method should have only 1 parameter: %s#%s",
                        classInfo.getName(), method.getName()));
            }
            Named named = (Named) methodInfo.getAnnotationInfos().stream()
                    .map(it -> it.getInherited(Named.class))
                    .filter(Objects::nonNull)
                    .map(AnnotationInfo::getAnnotation)
                    .findFirst()
                    .orElse(null);
            String componentName = named == null ? null : named.value();
            if(componentName == null || "".equals(componentName)) {
                Class<?> parameterType = method.getParameterTypes()[0];
                componentName = StringUtils.toLowerCase1st(parameterType.getSimpleName());
            }
            Object component = context.getComponent(componentName);
            if(component == null) {
                List<Pair<String, MethodInfo>> pendingMethod = pendingInjections
                        .computeIfAbsent(componentDefinition, it -> new ArrayList<>());
                pendingMethod.add(Pair.of(componentName, methodInfo));
                Set<ComponentDefinition<?>> pendingDefinitions = requiredComponents
                        .computeIfAbsent(componentName, it -> new HashSet<>());
                pendingDefinitions.add(componentDefinition);
                fullyInjected = false;
                continue;
            }
            methodInfo.invoke(componentDefinition.getComponentBaseInstance(), component);
        }
        if(fullyInjected) {
            context.addMark(componentDefinition.getName(), MethodInjector.class);
        }
    }

    private void doPendingInjections(ComponentDefinition<?> componentDefinition, IMutableContext context) throws Exception {
        String name = componentDefinition.getName();
        Set<ComponentDefinition<?>> definitions = requiredComponents.get(name);
        if(definitions == null) {
            return;
        }
        for (ComponentDefinition<?> definition : definitions) {
            List<Pair<String, MethodInfo>> fields = pendingInjections.get(definition).stream()
                    .filter(it -> it.getLeft().equals(name))
                    .collect(Collectors.toList());
            for (Pair<String, MethodInfo> fieldInfo : fields) {
                fieldInfo.getRight().invoke(definition.getComponentBaseInstance(),
                        componentDefinition.getComponentInstance());
            }
            pendingInjections.get(definition).removeAll(fields);
            if(pendingInjections.get(definition).isEmpty()) {
                pendingInjections.remove(definition);
                context.addMark(definition.getName(), MethodInjector.class);
            }
        }
        requiredComponents.remove(name);
    }
}
