package xyz.tiny.injector.post_init_private_in_parent;

import xyz.tiny.injector.annotation.PostInitialization;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
public abstract class AbstractComponent {
    final AtomicInteger atomicInteger = new AtomicInteger();


    @PostInitialization
    private void init() {
        atomicInteger.incrementAndGet();
    }
}
