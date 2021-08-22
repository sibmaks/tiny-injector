package xyz.tiny.injector.post_init_private_in_parent;

import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.annotation.PostInitialization;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class AComponent extends AbstractComponent {
    final AtomicInteger atomicInteger2 = new AtomicInteger();

    @PostInitialization
    public void init() {
        atomicInteger2.incrementAndGet();
    }
}
