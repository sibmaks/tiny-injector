package xyz.tiny.injector.context.base;

import xyz.tiny.injector.ComponentDefinition;
import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.context.IContext;
import xyz.tiny.injector.context.IMutableContext;
import xyz.tiny.injector.context.UpdateType;
import xyz.tiny.injector.context.listener.IContextListener;
import xyz.tiny.injector.reflection.AnnotationInfo;
import xyz.tiny.injector.reflection.ClassInfo;
import xyz.tiny.injector.reflection.MethodInfo;
import xyz.tiny.injector.utils.StringUtils;

import javax.inject.Named;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provider processor
 * Await until all providers will be fully injected after that call get method and add new component into context
 *
 * In case if some of required dependencies not injected throw an exception
 *
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
@Component
public class ProviderProcessor implements IContextListener {
    private Map<ComponentDefinition<?>, String> pendingProviders;

    @Override
    public void onCreated(IMutableContext mutableContext) {
        this.pendingProviders = new HashMap<>();
    }

    @Override
    public void onInitialized(IContext context) {
        if(pendingProviders.isEmpty()) {
            return;
        }
        throw new IllegalStateException(String.format("Provider is not fully initialized: %s",
                pendingProviders.keySet().stream()
                        .map(ComponentDefinition::getName)
                        .collect(Collectors.joining(", "))));
    }

    @Override
    public void onAddComponentDefinition(ComponentDefinition<?> componentDefinition, IMutableContext context) throws Exception {
        ClassInfo<?> componentClass = componentDefinition.getComponentClass();
        if(Provider.class.isAssignableFrom(componentClass.get())) {
            MethodInfo getMethod = componentClass.getMethodInfos().stream().filter(it -> it.getName().equals("get"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("get method not found in provider: " + componentClass.getName()));

            Named named = (Named) getMethod.getAnnotationInfos().stream()
                    .map(it -> it.getInherited(Named.class))
                    .filter(Objects::nonNull)
                    .map(AnnotationInfo::getAnnotation)
                    .findFirst()
                    .orElse(null);

            Class<?> methodReturnType = getMethod.getReturnType();
            String componentName = StringUtils.toLowerCase1st(methodReturnType.getSimpleName());
            if (named != null && !"".equals(named.value())) {
                componentName = named.value();
            }
            Object existingComponent = context.getComponent(componentName);
            if(existingComponent != null) {
                throw new IllegalStateException(String.format("Provider %s try override existing component %s",
                        componentClass.getName(), componentName));
            }

            if(componentDefinition.isMarked(FieldInjector.class, MethodInjector.class)) {
                Object component = ((Provider<?>) componentDefinition.getComponentInstance()).get();
                ClassInfo<Object> classInfo = ClassInfo.from((Class<Object>) component.getClass());
                context.add(componentName, classInfo, component);
            } else {
                pendingProviders.put(componentDefinition, componentName);
            }
        }
    }

    @Override
    public void onUpdated(UpdateType updateType, ComponentDefinition<?> componentDefinition, IMutableContext context) throws Exception {
        if(updateType != UpdateType.MARKED) {
            return;
        }
        String componentName = pendingProviders.get(componentDefinition);
        if(componentName != null) {
            Object existingComponent = context.getComponent(componentName);
            if(existingComponent != null) {
                throw new IllegalStateException(String.format("Provider %s try override existing component %s",
                        componentDefinition.getName(), componentName));
            }
            if(componentDefinition.isMarked(FieldInjector.class, MethodInjector.class)) {
                pendingProviders.remove(componentDefinition);
                Object component = ((Provider<?>) componentDefinition.getComponentInstance()).get();
                ClassInfo<Object> classInfo = ClassInfo.from((Class<Object>) component.getClass());
                context.add(componentName, classInfo, component);
            }
        }
    }
}
