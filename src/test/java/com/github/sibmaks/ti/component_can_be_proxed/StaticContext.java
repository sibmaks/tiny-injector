package com.github.sibmaks.ti.component_can_be_proxed;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
public class StaticContext {
    static final AtomicInteger proxyCalls = new AtomicInteger();
}
