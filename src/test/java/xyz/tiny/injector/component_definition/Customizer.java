package xyz.tiny.injector.component_definition;

import xyz.tiny.injector.ComponentDefinition;
import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.context.IMutableContext;
import xyz.tiny.injector.context.listener.IContextListener;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class Customizer implements IContextListener {
    @Override
    public void onAddComponentDefinition(ComponentDefinition<?> componentDefinition, IMutableContext context) throws Exception {
        if("aComponent".equals(componentDefinition.getName())) {
            AComponent component = new AComponent();
            component.name = "dog";
            component.bComponent = ((AComponent)componentDefinition.getComponentInstance()).bComponent;
            context.update(componentDefinition.getName(), component);
        }
    }
}
