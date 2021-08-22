package xyz.tiny.injector.context.base;

import xyz.tiny.injector.ComponentDefinition;
import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.context.IContext;
import xyz.tiny.injector.context.IMutableContext;
import xyz.tiny.injector.context.UpdateType;
import xyz.tiny.injector.context.listener.IContextListener;
import xyz.tiny.injector.exception.MethodInjectionException;
import xyz.tiny.injector.reflection.AnnotationInfo;
import xyz.tiny.injector.reflection.ClassInfo;
import xyz.tiny.injector.reflection.MethodInfo;
import xyz.tiny.injector.utils.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Method dependency injector, execute method injections for all components
 * In case of component updated, all depended on component's method will be injected again.
 *
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
@Component
public class MethodInjector implements IContextListener {
    private Map<ComponentDefinition<?>, Set<InjectableMethod>> register;
    private Map<String, Map<ComponentDefinition<?>, Set<InjectableMethod>>> depending;
    private Map<String, Set<ComponentDefinition<?>>> requiredComponents;

    @Override
    public void onCreated(IMutableContext mutableContext) {
        this.register = new HashMap<>();
        this.requiredComponents = new HashMap<>();
        this.depending = new HashMap<>();
    }

    @Override
    public void onInitialized(IContext context) {
        depending.clear();
        register.clear();
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
            if (methodInfo.getAnnotationInfos().stream().noneMatch(it -> it.isInherited(Inject.class))) {
                continue;
            }
            Method method = methodInfo.getMethod();
            if (method.getParameterCount() == 0) {
                throw new MethodInjectionException(String.format("Injected method should accept at least one parameter: %s#%s",
                        classInfo.getName(), method.getName()));
            }
            List<Object> args = new ArrayList<>();
            Set<InjectableMethod> injectableMethods = register.computeIfAbsent(componentDefinition, it -> new HashSet<>());
            InjectableMethod injectableMethod = new InjectableMethod(methodInfo);
            injectableMethods.add(injectableMethod);
            for (int i = 0; i < method.getParameterCount(); i++) {
                Named named = (Named) Arrays.stream(method.getParameterAnnotations()[i])
                        .map(AnnotationInfo::from)
                        .map(it -> it.getInherited(Named.class))
                        .filter(Objects::nonNull)
                        .map(AnnotationInfo::getAnnotation)
                        .findFirst()
                        .orElse(null);
                String componentName = named == null ? null : named.value();
                if (componentName == null || "".equals(componentName)) {
                    Class<?> parameterType = method.getParameterTypes()[i];
                    componentName = StringUtils.toLowerCase1st(parameterType.getSimpleName());
                }

                Map<ComponentDefinition<?>, Set<InjectableMethod>> componentDefinitionListMap = depending
                        .computeIfAbsent(componentName, it -> new HashMap<>());
                componentDefinitionListMap.computeIfAbsent(componentDefinition, it -> new HashSet<>())
                        .add(injectableMethod);
                injectableMethod.requiredComponents.add(componentName);

                Object component = context.getComponent(componentName);
                if (component == null) {
                    injectableMethod.pendingComponents.add(componentName);
                    Set<ComponentDefinition<?>> pendingDefinitions = requiredComponents
                            .computeIfAbsent(componentName, it -> new HashSet<>());
                    pendingDefinitions.add(componentDefinition);
                    fullyInjected = false;
                } else {
                    args.add(component);
                }
            }
            if (args.size() == method.getParameterCount()) {
                methodInfo.invoke(componentDefinition.getComponentBaseInstance(), args.toArray());
            }
        }
        if (fullyInjected) {
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
            Set<InjectableMethod> injectableMethodSet = register.get(definition);
            Set<InjectableMethod> injectableMethods = injectableMethodSet.stream()
                    .filter(it -> it.pendingComponents.contains(name))
                    .collect(Collectors.toSet());
            for (InjectableMethod injectableMethod : injectableMethods) {
                injectableMethod.pendingComponents.remove(name);
                if(injectableMethod.pendingComponents.isEmpty()) {
                    injectableMethod.invoke(definition.getComponentInstance(), context);
                }
            }
            if(injectableMethodSet.stream().allMatch(it -> it.pendingComponents.isEmpty())) {
                context.addMark(definition.getName(), MethodInjector.class);
            }
        }
        requiredComponents.remove(name);
    }

    @Override
    public void onUpdated(UpdateType updateType, ComponentDefinition<?> componentDefinition, IMutableContext context) {
        if(updateType != UpdateType.INSTANCE_CHANGED) {
            return;
        }
        Map<ComponentDefinition<?>, Set<InjectableMethod>> componentDefinitionListMap = depending.get(componentDefinition.getName());
        if(componentDefinitionListMap == null) {
            return;
        }
        for (Map.Entry<ComponentDefinition<?>, Set<InjectableMethod>> entry : componentDefinitionListMap.entrySet()) {
            ComponentDefinition<?> definition = entry.getKey();
            for (InjectableMethod methodInfo : entry.getValue()) {
                if(methodInfo.pendingComponents.isEmpty()) {
                    methodInfo.invoke(definition.getComponentInstance(), context);
                }
            }
        }
    }

    private static class InjectableMethod {
        final MethodInfo methodInfo;
        final List<String> requiredComponents;
        final Set<String> pendingComponents;

        private InjectableMethod(MethodInfo methodInfo) {
            this.methodInfo = methodInfo;
            this.requiredComponents = new ArrayList<>();
            this.pendingComponents = new HashSet<>();
        }

        void invoke(Object source, IContext context) {
            Object[] args = requiredComponents.stream().map(context::getComponent).toArray();
            methodInfo.invoke(source, args);
        }
    }
}
