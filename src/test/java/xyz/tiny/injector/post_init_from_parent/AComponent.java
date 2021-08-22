package xyz.tiny.injector.post_init_from_parent;

import xyz.tiny.injector.annotation.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
@Component
public class AComponent extends AbstractComponent {
    final AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public void init() {
        atomicInteger.incrementAndGet();
    }
}
