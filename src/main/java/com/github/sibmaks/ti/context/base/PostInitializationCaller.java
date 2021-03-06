package com.github.sibmaks.ti.context.base;

import com.github.sibmaks.ti.context.IContext;
import com.github.sibmaks.ti.ComponentDefinition;
import com.github.sibmaks.ti.annotation.Component;
import com.github.sibmaks.ti.annotation.PostInitialization;
import com.github.sibmaks.ti.context.IMutableContext;
import com.github.sibmaks.ti.context.listener.IContextListener;
import com.github.sibmaks.ti.reflection.ClassInfo;
import com.github.sibmaks.ti.reflection.MethodInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Post initialization method caller, collect methods annotated with PostInitialization and call them after context initialized
 *
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class PostInitializationCaller implements IContextListener {
    private Map<ComponentDefinition<?>, List<MethodInfo>> componentInitializers;

    @Override
    public void onCreated(IMutableContext mutableContext) {
        this.componentInitializers = new HashMap<>();
    }

    @Override
    public void onInitialized(IContext context) {
        for (Map.Entry<ComponentDefinition<?>, List<MethodInfo>> entry : componentInitializers.entrySet()) {
            Object instance = entry.getKey().getComponentInstance();
            for (MethodInfo methodInfo : entry.getValue()) {
                methodInfo.invoke(instance);
            }
        }
        componentInitializers.clear();
    }

    @Override
    public void onAddComponentDefinition(ComponentDefinition<?> componentDefinition, IMutableContext context) {
        ClassInfo<?> classInfo = componentDefinition.getComponentClass();
        for (MethodInfo methodInfo : classInfo.getMethodInfos()) {
            if(methodInfo.getAnnotationInfos().stream().noneMatch(it -> it.isInherited(PostInitialization.class))) {
                continue;
            }
            Method method = methodInfo.getMethod();
            if(method.getParameterCount() != 0) {
                throw new IllegalStateException(String.format("Post initialization method should not have any parameters: %s#%s",
                        classInfo.getName(), method.getName()));
            }

            List<MethodInfo> methodInfos = componentInitializers.computeIfAbsent(componentDefinition, it -> new ArrayList<>());
            methodInfos.add(methodInfo);
        }
    }
}
