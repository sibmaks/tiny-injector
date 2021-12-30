package com.github.sibmaks.ti.context.base;

import com.github.sibmaks.ti.context.base.MethodInjector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.github.sibmaks.ti.ComponentDefinition;
import com.github.sibmaks.ti.context.IMutableContext;
import com.github.sibmaks.ti.context.UpdateType;
import com.github.sibmaks.ti.reflection.ClassInfo;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
class MethodInjectorTest {
    @Test
    public void methodReInjectedIfComponentChanged() throws Exception {
        MethodInjector methodInjector = new MethodInjector();

        IMutableContext mutableContext = Mockito.mock(IMutableContext.class);

        methodInjector.onCreated(mutableContext);

        ComponentDefinition<AComponent> aComponent = buildComponentDefinition("aComponent", new AComponent(), AComponent.class);
        ComponentDefinition<BComponent> bComponent1st = buildComponentDefinition("bComponent", new BComponent("1st"), BComponent.class);
        ComponentDefinition<BComponent> bComponent2nd = buildComponentDefinition("bComponent", new BComponent("2nd"), BComponent.class);
        ComponentDefinition<CComponent> cComponent = buildComponentDefinition("cComponent", new CComponent(), CComponent.class);
        ComponentDefinition<DComponent> dComponent = buildComponentDefinition("dComponent", new DComponent(), DComponent.class);


        Mockito.when(mutableContext.getComponent("bComponent"))
                .thenReturn(bComponent1st.getComponentInstance())
                .thenReturn(bComponent2nd.getComponentInstance());

        methodInjector.onAddComponentDefinition(aComponent, mutableContext);

        Assertions.assertEquals(1, aComponent.getComponentInstance().atomicInteger.get());
        Assertions.assertEquals("1st", aComponent.getComponentInstance().bComponent.name);
        Assertions.assertEquals(bComponent1st.getComponentInstance(), aComponent.getComponentInstance().bComponent);

        Mockito.when(mutableContext.getComponent("cComponent"))
                .thenReturn(cComponent.getComponentInstance());
        methodInjector.onAddComponentDefinition(cComponent, mutableContext);

        methodInjector.onUpdated(UpdateType.INSTANCE_CHANGED, bComponent2nd, mutableContext);

        Assertions.assertEquals(2, aComponent.getComponentInstance().atomicInteger.get());
        Assertions.assertEquals("2nd", aComponent.getComponentInstance().bComponent.name);
        Assertions.assertEquals(bComponent2nd.getComponentInstance(), aComponent.getComponentInstance().bComponent);
        Assertions.assertEquals(cComponent.getComponentInstance(), aComponent.getComponentInstance().cComponent);

        methodInjector.onUpdated(UpdateType.INSTANCE_CHANGED, cComponent, mutableContext);

        Assertions.assertEquals(2, aComponent.getComponentInstance().atomicInteger.get());
        Assertions.assertEquals("2nd", aComponent.getComponentInstance().bComponent.name);
        Assertions.assertEquals(bComponent2nd.getComponentInstance(), aComponent.getComponentInstance().bComponent);
        Assertions.assertEquals(cComponent.getComponentInstance(), aComponent.getComponentInstance().cComponent);

        methodInjector.onUpdated(UpdateType.INSTANCE_CHANGED, dComponent, mutableContext);

        Assertions.assertEquals(2, aComponent.getComponentInstance().atomicInteger.get());
        Assertions.assertEquals("2nd", aComponent.getComponentInstance().bComponent.name);
        Assertions.assertEquals(bComponent2nd.getComponentInstance(), aComponent.getComponentInstance().bComponent);
        Assertions.assertEquals(cComponent.getComponentInstance(), aComponent.getComponentInstance().cComponent);
    }

    @Test
    public void methodPendingInjection() throws Exception {
        MethodInjector methodInjector = new MethodInjector();

        IMutableContext mutableContext = Mockito.mock(IMutableContext.class);

        methodInjector.onCreated(mutableContext);

        ComponentDefinition<SeveralInjectsComponent> component = buildComponentDefinition("severalInjectsComponent", new SeveralInjectsComponent(), SeveralInjectsComponent.class);
        ComponentDefinition<BComponent> bComponent = buildComponentDefinition("bComponent", new BComponent("any"), BComponent.class);
        ComponentDefinition<CComponent> cComponent = buildComponentDefinition("cComponent", new CComponent(), CComponent.class);

        methodInjector.onAddComponentDefinition(component, mutableContext);

        Assertions.assertNull(component.getComponentInstance().bComponent);
        Assertions.assertNull(component.getComponentInstance().cComponent);

        Mockito.when(mutableContext.getComponent("bComponent")).thenReturn(bComponent.getComponentInstance());

        methodInjector.onAddComponentDefinition(bComponent, mutableContext);

        Assertions.assertNull(component.getComponentInstance().bComponent);
        Assertions.assertNull(component.getComponentInstance().cComponent);

        methodInjector.onUpdated(UpdateType.INSTANCE_CHANGED, bComponent, mutableContext);

        Assertions.assertNull(component.getComponentInstance().bComponent);
        Assertions.assertNull(component.getComponentInstance().cComponent);

        Mockito.when(mutableContext.getComponent("cComponent")).thenReturn(cComponent.getComponentInstance());

        methodInjector.onAddComponentDefinition(cComponent, mutableContext);

        Assertions.assertEquals(bComponent.getComponentInstance(), component.getComponentInstance().bComponent);
        Assertions.assertEquals(cComponent.getComponentInstance(), component.getComponentInstance().cComponent);

    }

    private<T> ComponentDefinition<T> buildComponentDefinition(String name, T object, Class<T> clazz) {
        return new ComponentDefinition<>(name, ClassInfo.from(clazz), object);
    }

    static class AComponent {
        BComponent bComponent;
        CComponent cComponent;
        final AtomicInteger atomicInteger = new AtomicInteger();

        @Inject
        public void set(BComponent bComponent) {
            this.bComponent = bComponent;
            atomicInteger.incrementAndGet();
        }

        @Inject
        public void set2(CComponent cComponent) {
            this.cComponent = cComponent;
        }
    }

    static class SeveralInjectsComponent {
        BComponent bComponent;
        CComponent cComponent;

        @Inject
        public void set(BComponent bComponent, CComponent cComponent) {
            this.bComponent = bComponent;
            this.cComponent = cComponent;
        }
    }

    static class BComponent {
        final String name;

        public BComponent(String name) {
            this.name = name;
        }
    }

    static class CComponent {

    }

    static class DComponent {

    }
}