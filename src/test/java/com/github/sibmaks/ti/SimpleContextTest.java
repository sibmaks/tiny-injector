package com.github.sibmaks.ti;

import com.github.sibmaks.ti.context.IMutableContext;
import com.github.sibmaks.ti.context.UpdateType;
import com.github.sibmaks.ti.context.listener.IContextListener;
import com.github.sibmaks.ti.reflection.ClassInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
class SimpleContextTest {
    @Test
    void cantAddComponentWhenContextIsInitialized() {
        SimpleContext simpleContext = new SimpleContext(Collections.emptyList());
        simpleContext.onInitializationFinished();
        Assertions.assertThrows(IllegalStateException.class, () -> simpleContext.add("any", null, null));
    }

    @Test
    void cantUpdateComponentWhenContextIsInitialized() {
        SimpleContext simpleContext = new SimpleContext(Collections.emptyList());
        simpleContext.onInitializationFinished();
        Assertions.assertThrows(IllegalStateException.class, () -> simpleContext.update("any", null));
    }

    @Test
    void cantMarkComponentWhenContextIsInitialized() {
        SimpleContext simpleContext = new SimpleContext(Collections.emptyList());
        simpleContext.onInitializationFinished();
        Assertions.assertThrows(IllegalStateException.class, () -> simpleContext.addMark("any", null));
    }

    @Test
    void onAddDuplicateMarkListenerWontCalled() {
        AtomicInteger atomicInteger = new AtomicInteger();

        IContextListener listener = new IContextListener() {
            @Override
            public void onUpdated(UpdateType updateType, ComponentDefinition<?> componentDefinition, IMutableContext context) {
                atomicInteger.incrementAndGet();
            }
        };

        SimpleContext simpleContext = new SimpleContext(Collections.singletonList(listener));
        simpleContext.add("any", ClassInfo.from(Component.class), new Component());
        simpleContext.addMark("any", "mark1");
        simpleContext.addMark("any", "mark1");
        Assertions.assertEquals(1, atomicInteger.get());
    }

    @Test
    void ifUpdateNotChangeInstanceListenerNotCalled() {
        AtomicInteger atomicInteger = new AtomicInteger();

        IContextListener listener = new IContextListener() {
            @Override
            public void onUpdated(UpdateType updateType, ComponentDefinition<?> componentDefinition, IMutableContext context) {
                atomicInteger.incrementAndGet();
            }
        };

        SimpleContext simpleContext = new SimpleContext(Collections.singletonList(listener));
        Component component = new Component();
        simpleContext.add("any", ClassInfo.from(Component.class), component);
        simpleContext.update("any", component);
        Assertions.assertEquals(0, atomicInteger.get());
    }

    static class Component {

    }
}