package xyz.tiny.injector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.context.IMutableContext;
import xyz.tiny.injector.context.UpdateType;
import xyz.tiny.injector.context.listener.IContextListener;
import xyz.tiny.injector.reflection.ClassInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
class SimpleContextTest {
    @Test
    public void cantAddComponentWhenContextIsInitialized() throws Exception {
        SimpleContext simpleContext = new SimpleContext(Collections.emptyList());
        simpleContext.onInitializationFinished();
        Assertions.assertThrows(IllegalStateException.class, () -> simpleContext.add("any", null, null));
    }

    @Test
    public void cantUpdateComponentWhenContextIsInitialized() throws Exception {
        SimpleContext simpleContext = new SimpleContext(Collections.emptyList());
        simpleContext.onInitializationFinished();
        Assertions.assertThrows(IllegalStateException.class, () -> simpleContext.update("any", null));
    }

    @Test
    public void cantMarkComponentWhenContextIsInitialized() throws Exception {
        SimpleContext simpleContext = new SimpleContext(Collections.emptyList());
        simpleContext.onInitializationFinished();
        Assertions.assertThrows(IllegalStateException.class, () -> simpleContext.addMark("any", null));
    }

    @Test
    public void onAddDuplicateMarkListenerWontCalled() throws Exception {
        AtomicInteger atomicInteger = new AtomicInteger();

        IContextListener listener = new IContextListener() {
            @Override
            public void onUpdated(UpdateType updateType, ComponentDefinition<?> componentDefinition, IMutableContext context) {
                atomicInteger.incrementAndGet();
            }
        };

        SimpleContext simpleContext = new SimpleContext(Arrays.asList(listener));
        simpleContext.add("any", ClassInfo.from(Component.class), new Component());
        simpleContext.addMark("any", "mark1");
        simpleContext.addMark("any", "mark1");
        Assertions.assertEquals(1, atomicInteger.get());
    }

    @Test
    public void ifUpdateNotChangeInstanceListenerNotCalled() throws Exception {
        AtomicInteger atomicInteger = new AtomicInteger();

        IContextListener listener = new IContextListener() {
            @Override
            public void onUpdated(UpdateType updateType, ComponentDefinition<?> componentDefinition, IMutableContext context) {
                atomicInteger.incrementAndGet();
            }
        };

        SimpleContext simpleContext = new SimpleContext(Arrays.asList(listener));
        Component component = new Component();
        simpleContext.add("any", ClassInfo.from(Component.class), component);
        simpleContext.update("any", component);
        Assertions.assertEquals(0, atomicInteger.get());
    }

    static class Component {

    }
}