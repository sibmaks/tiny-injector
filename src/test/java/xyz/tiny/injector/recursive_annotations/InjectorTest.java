package xyz.tiny.injector.recursive_annotations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.tiny.injector.Injector;
import xyz.tiny.injector.context.IContext;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InjectorTest {

    @Test
    void recursiveAnnotationSupported() throws Throwable {
        IContext iContext = Injector.buildInjections(InjectorTest.class.getPackage().getName());
        AComponent aComponent = iContext.getComponent("aComponent");
        Assertions.assertNotNull(aComponent);
    }
}