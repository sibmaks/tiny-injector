package com.github.sibmaks.ti.component_definition;

import com.github.sibmaks.ti.ComponentDefinition;
import com.github.sibmaks.ti.annotation.Component;
import com.github.sibmaks.ti.context.IMutableContext;
import com.github.sibmaks.ti.context.listener.IContextListener;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class Customizer implements IContextListener {
    @Override
    public void onAddComponentDefinition(ComponentDefinition<?> componentDefinition, IMutableContext context) {
        if("aComponent".equals(componentDefinition.getName())) {
            AComponent component = new AComponent();
            component.name = "dog";
            component.bComponent = ((AComponent)componentDefinition.getComponentInstance()).bComponent;
            context.update(componentDefinition.getName(), component);
        }
    }
}
