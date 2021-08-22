package xyz.tiny.injector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

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

}