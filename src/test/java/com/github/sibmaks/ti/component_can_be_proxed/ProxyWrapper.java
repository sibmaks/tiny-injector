package com.github.sibmaks.ti.component_can_be_proxed;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import com.github.sibmaks.ti.ComponentDefinition;
import com.github.sibmaks.ti.annotation.Component;
import com.github.sibmaks.ti.context.IMutableContext;
import com.github.sibmaks.ti.context.listener.IContextListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class ProxyWrapper implements IContextListener {
    static final List<String> proxied = new ArrayList<>();

    @Override
    public void onAddComponentDefinition(ComponentDefinition<?> componentDefinition, IMutableContext context) {
        Object instance = componentDefinition.getComponentInstance();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(componentDefinition.getComponentClass().get());
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            StaticContext.proxyCalls.incrementAndGet();
            return proxy.invoke(instance, args);
        });
        proxied.add(componentDefinition.getName());
        context.update(componentDefinition.getName(), enhancer.create());
    }
}
