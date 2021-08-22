package xyz.tiny.injector.context.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xyz.tiny.injector.ComponentDefinition;
import xyz.tiny.injector.context.IMutableContext;
import xyz.tiny.injector.reflection.ClassInfo;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
class ProviderInjectorTest {
    @Test
    public void exceptionIfPendingProviderNotInitialized() throws Exception {
        ProviderInjector providerInjector = new ProviderInjector();

        ComponentDefinition<AProvider> componentDefinition = buildComponentDefinition("stub", new AProvider(), AProvider.class);

        IMutableContext mutableContext = Mockito.mock(IMutableContext.class);

        providerInjector.onCreated(mutableContext);

        providerInjector.onAddComponentDefinition(componentDefinition, mutableContext);

        Assertions.assertThrows(IllegalStateException.class, () -> providerInjector.onInitialized(mutableContext));
    }

    @Test
    public void providerCantOverrideExistingComponents() {
        ProviderInjector providerInjector = new ProviderInjector();

        ComponentDefinition<AProvider> componentDefinition = buildComponentDefinition("stub", new AProvider(), AProvider.class);

        IMutableContext mutableContext = Mockito.mock(IMutableContext.class);
        Mockito.when(mutableContext.getComponent("providedComponent")).thenReturn(componentDefinition.getComponentInstance());

        providerInjector.onCreated(mutableContext);

        Assertions.assertThrows(IllegalStateException.class, () -> providerInjector.onAddComponentDefinition(componentDefinition, mutableContext));
    }

    @Test
    public void providerCalledIfInjectionComplete() throws Exception {
        ProviderInjector providerInjector = new ProviderInjector();

        ComponentDefinition<AProvider> componentDefinition = buildComponentDefinition("stub", new AProvider(), AProvider.class);
        Field field = ComponentDefinition.class.getDeclaredField("marks");
        field.setAccessible(true);
        Set<Object> marks = (Set<Object>) field.get(componentDefinition);
        marks.add(FieldInjector.class);
        marks.add(MethodInjector.class);

        IMutableContext mutableContext = Mockito.mock(IMutableContext.class);

        providerInjector.onCreated(mutableContext);

        providerInjector.onAddComponentDefinition(componentDefinition, mutableContext);

        Mockito.verify(mutableContext, Mockito.times(1)).add(Mockito.eq("providedComponent"), Mockito.any(), Mockito.any());
    }

    @Test
    public void providerCallPendingIfNotFullyInjected() throws Exception {
        ProviderInjector providerInjector = new ProviderInjector();

        ComponentDefinition<AProvider> componentDefinition = buildComponentDefinition("stub", new AProvider(), AProvider.class);
        Field field = ComponentDefinition.class.getDeclaredField("marks");
        field.setAccessible(true);
        Set<Object> marks = (Set<Object>) field.get(componentDefinition);
        marks.add(FieldInjector.class);

        IMutableContext mutableContext = Mockito.mock(IMutableContext.class);

        providerInjector.onCreated(mutableContext);

        providerInjector.onAddComponentDefinition(componentDefinition, mutableContext);

        Mockito.verify(mutableContext, Mockito.never()).add(Mockito.eq("providedComponent"), Mockito.any(), Mockito.any());

        providerInjector.onUpdated(componentDefinition, mutableContext);

        Mockito.verify(mutableContext, Mockito.never()).add(Mockito.eq("providedComponent"), Mockito.any(), Mockito.any());

        marks.add(MethodInjector.class);

        providerInjector.onUpdated(componentDefinition, mutableContext);

        Mockito.verify(mutableContext, Mockito.times(1)).add(Mockito.eq("providedComponent"), Mockito.any(), Mockito.any());
    }

    @Test
    public void providerCallPendingCantOverrideExisting() throws Exception {
        ProviderInjector providerInjector = new ProviderInjector();

        ComponentDefinition<AProvider> componentDefinition = buildComponentDefinition("stub", new AProvider(), AProvider.class);
        Field field = ComponentDefinition.class.getDeclaredField("marks");
        field.setAccessible(true);
        Set<Object> marks = (Set<Object>) field.get(componentDefinition);
        marks.add(FieldInjector.class);

        IMutableContext mutableContext = Mockito.mock(IMutableContext.class);

        providerInjector.onCreated(mutableContext);

        providerInjector.onAddComponentDefinition(componentDefinition, mutableContext);

        Mockito.verify(mutableContext, Mockito.never()).add(Mockito.eq("providedComponent"), Mockito.any(), Mockito.any());

        Mockito.when(mutableContext.getComponent("providedComponent")).thenReturn(componentDefinition.getComponentInstance());

        Assertions.assertThrows(IllegalStateException.class, () -> providerInjector.onUpdated(componentDefinition, mutableContext));
    }

    private<T> ComponentDefinition<T> buildComponentDefinition(String name, T object, Class<T> clazz) {
        return new ComponentDefinition<>(name, ClassInfo.from(clazz), object);
    }
}