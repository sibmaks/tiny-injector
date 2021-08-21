package xyz.tiny.injector.context.base;

import xyz.tiny.injector.ComponentDefinition;
import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.context.IContext;
import xyz.tiny.injector.context.IMutableContext;
import xyz.tiny.injector.context.listener.IContextListener;
import xyz.tiny.injector.exception.FieldInjectionException;
import xyz.tiny.injector.reflection.AnnotationInfo;
import xyz.tiny.injector.reflection.ClassInfo;
import xyz.tiny.injector.reflection.FieldInfo;
import xyz.tiny.injector.utils.Pair;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
@Component
public class FieldInjector implements IContextListener {
    private Map<String, Map<ComponentDefinition<?>, List<FieldInfo>>> depending;
    // pending field injections, contains not injected component name and field
    private Map<ComponentDefinition<?>, List<Pair<String, FieldInfo>>> pendingInjections;
    // key is required components, value is list of components that awaiting of component
    private Map<String, Set<ComponentDefinition<?>>> requiredComponents;

    @Override
    public void onCreated(IMutableContext mutableContext) {
        this.pendingInjections = new HashMap<>();
        this.requiredComponents = new HashMap<>();
        this.depending = new HashMap<>();
    }

    @Override
    public void onInitialized(IContext context) {
        depending.clear();
        if(requiredComponents.isEmpty()) {
            return;
        }
        throw new FieldInjectionException(String.format("Components not found for field injections: %s",
                String.join(", ", requiredComponents.keySet())));
    }

    @Override
    public void onAddComponentDefinition(ComponentDefinition<?> componentDefinition, IMutableContext context) throws Exception {
        doComponentInjections(componentDefinition, context);
        doPendingInjections(componentDefinition);
    }

    private void doComponentInjections(ComponentDefinition<?> componentDefinition,
                                       IMutableContext context) throws Exception {
        ClassInfo<?> classInfo = componentDefinition.getComponentClass();
        boolean fullyInjected = true;
        for (FieldInfo fieldInfo : classInfo.getFieldInfos()) {
            if(fieldInfo.getAnnotationInfos().stream().noneMatch(it -> it.isInherited(Inject.class))) {
                continue;
            }
            if(fieldInfo.isFinal()) {
                throw new FieldInjectionException(String.format("Can't inject final field: %s.%s",
                        classInfo.getName(), fieldInfo.getName()));
            }
            Named named = (Named) fieldInfo.getAnnotationInfos().stream()
                    .map(it -> it.getInherited(Named.class))
                    .filter(Objects::nonNull)
                    .map(AnnotationInfo::getAnnotation)
                    .findFirst()
                    .orElse(null);
            String componentName = named == null || "".equals(named.value()) ? fieldInfo.getName() : named.value();

            Map<ComponentDefinition<?>, List<FieldInfo>> componentDefinitionListMap = depending.computeIfAbsent(componentName, it -> new HashMap<>());
            List<FieldInfo> fieldInfos = componentDefinitionListMap.computeIfAbsent(componentDefinition, it -> new ArrayList<>());
            fieldInfos.add(fieldInfo);

            Object component = context.getComponent(componentName);
            if(component == null) {
                List<Pair<String, FieldInfo>> pendingField = pendingInjections
                        .computeIfAbsent(componentDefinition, it -> new ArrayList<>());
                pendingField.add(Pair.of(componentName, fieldInfo));
                Set<ComponentDefinition<?>> pendingDefinitions = requiredComponents
                        .computeIfAbsent(componentName, it -> new HashSet<>());
                pendingDefinitions.add(componentDefinition);
                fullyInjected = false;
                continue;
            }
            fieldInfo.set(componentDefinition.getComponentBaseInstance(), component);
        }
        if(fullyInjected) {
            componentDefinition.mark(FieldInjector.class);
        }
    }

    private void doPendingInjections(ComponentDefinition<?> componentDefinition) throws Exception {
        String name = componentDefinition.getName();
        Set<ComponentDefinition<?>> definitions = requiredComponents.get(name);
        if(definitions == null) {
            return;
        }
        for (ComponentDefinition<?> definition : definitions) {
            List<Pair<String, FieldInfo>> fields = pendingInjections.get(definition).stream()
                    .filter(it -> it.getLeft().equals(name))
                    .collect(Collectors.toList());
            for (Pair<String, FieldInfo> fieldInfo : fields) {
                fieldInfo.getRight().set(definition.getComponentBaseInstance(), componentDefinition.getComponentInstance());
            }
            pendingInjections.get(definition).removeAll(fields);
            if(pendingInjections.get(definition).isEmpty()) {
                pendingInjections.remove(definition);
                definition.mark(FieldInjector.class);
            }
        }
        requiredComponents.remove(name);
    }

    @Override
    public void onUpdated(ComponentDefinition<?> componentDefinition, IMutableContext context) throws Exception {
        Map<ComponentDefinition<?>, List<FieldInfo>> componentDefinitionListMap = depending.get(componentDefinition.getName());
        if(componentDefinitionListMap == null || componentDefinitionListMap.isEmpty()) {
            return;
        }
        for (Map.Entry<ComponentDefinition<?>, List<FieldInfo>> entry : componentDefinitionListMap.entrySet()) {
            ComponentDefinition<?> definition = entry.getKey();
            for (FieldInfo fieldInfo : entry.getValue()) {
                fieldInfo.set(definition.getComponentInstance(), componentDefinition.getComponentInstance());
            }
        }
    }
}
