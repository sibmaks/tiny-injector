package com.github.sibmaks.ti.context.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.github.sibmaks.ti.ComponentDefinition;
import com.github.sibmaks.ti.context.IMutableContext;
import com.github.sibmaks.ti.context.UpdateType;
import com.github.sibmaks.ti.reflection.ClassInfo;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
class ProviderInjectorTest {
    @Test
    void exceptionIfPendingProviderNotInitialized() {
        ProviderProcessor providerProcessor = new ProviderProcessor();

        ComponentDefinition<AProvider> componentDefinition = buildComponentDefinition("stub", new AProvider(), AProvider.class);

        IMutableContext mutableContext = Mockito.mock(IMutableContext.class);

        providerProcessor.onCreated(mutableContext);

        providerProcessor.onAddComponentDefinition(componentDefinition, mutableContext);

        Assertions.assertThrows(IllegalStateException.class, () -> providerProcessor.onInitialized(mutableContext));
    }

    @Test
    void providerCantOverrideExistingComponents() {
        ProviderProcessor providerProcessor = new ProviderProcessor();

        ComponentDefinition<AProvider> componentDefinition = buildComponentDefinition("stub", new AProvider(), AProvider.class);

        IMutableContext mutableContext = Mockito.mock(IMutableContext.class);
        Mockito.when(mutableContext.getComponent("providedComponent")).thenReturn(componentDefinition.getComponentInstance());

        providerProcessor.onCreated(mutableContext);

        Assertions.assertThrows(IllegalStateException.class, () -> providerProcessor.onAddComponentDefinition(componentDefinition, mutableContext));
    }

    @Test
    void providerCalledIfInjectionComplete() throws Exception {
        ProviderProcessor providerProcessor = new ProviderProcessor();

        ComponentDefinition<AProvider> componentDefinition = buildComponentDefinition("stub", new AProvider(), AProvider.class);
        Field field = ComponentDefinition.class.getDeclaredField("marks");
        field.setAccessible(true);
        Set<Object> marks = (Set<Object>) field.get(componentDefinition);
        marks.add(FieldInjector.class);
        marks.add(MethodInjector.class);

        IMutableContext mutableContext = Mockito.mock(IMutableContext.class);

        providerProcessor.onCreated(mutableContext);

        providerProcessor.onAddComponentDefinition(componentDefinition, mutableContext);

        Mockito.verify(mutableContext, Mockito.times(1)).add(Mockito.eq("providedComponent"), Mockito.any(), Mockito.any());
    }

    @Test
    void providerCallPendingIfNotFullyInjected() throws Exception {
        ProviderProcessor providerProcessor = new ProviderProcessor();

        ComponentDefinition<AProvider> componentDefinition = buildComponentDefinition("stub", new AProvider(), AProvider.class);
        Field field = ComponentDefinition.class.getDeclaredField("marks");
        field.setAccessible(true);
        Set<Object> marks = (Set<Object>) field.get(componentDefinition);
        marks.add(FieldInjector.class);

        IMutableContext mutableContext = Mockito.mock(IMutableContext.class);

        providerProcessor.onCreated(mutableContext);

        providerProcessor.onAddComponentDefinition(componentDefinition, mutableContext);

        Mockito.verify(mutableContext, Mockito.never()).add(Mockito.eq("providedComponent"), Mockito.any(), Mockito.any());

        providerProcessor.onUpdated(UpdateType.MARKED, componentDefinition, mutableContext);

        Mockito.verify(mutableContext, Mockito.never()).add(Mockito.eq("providedComponent"), Mockito.any(), Mockito.any());

        marks.add(MethodInjector.class);

        providerProcessor.onUpdated(UpdateType.MARKED, componentDefinition, mutableContext);

        Mockito.verify(mutableContext, Mockito.times(1)).add(Mockito.eq("providedComponent"), Mockito.any(), Mockito.any());
    }

    @Test
    void providerCallPendingCantOverrideExisting() throws Exception {
        ProviderProcessor providerProcessor = new ProviderProcessor();

        ComponentDefinition<AProvider> componentDefinition = buildComponentDefinition("stub", new AProvider(), AProvider.class);
        Field field = ComponentDefinition.class.getDeclaredField("marks");
        field.setAccessible(true);
        Set<Object> marks = (Set<Object>) field.get(componentDefinition);
        marks.add(FieldInjector.class);

        IMutableContext mutableContext = Mockito.mock(IMutableContext.class);

        providerProcessor.onCreated(mutableContext);

        providerProcessor.onAddComponentDefinition(componentDefinition, mutableContext);

        Mockito.verify(mutableContext, Mockito.never()).add(Mockito.eq("providedComponent"), Mockito.any(), Mockito.any());

        Mockito.when(mutableContext.getComponent("providedComponent")).thenReturn(componentDefinition.getComponentInstance());

        Assertions.assertThrows(IllegalStateException.class, () -> providerProcessor.onUpdated(UpdateType.MARKED, componentDefinition, mutableContext));
    }

    private<T> ComponentDefinition<T> buildComponentDefinition(String name, T object, Class<T> clazz) {
        return new ComponentDefinition<>(name, ClassInfo.from(clazz), object);
    }
}