package xyz.tiny.injector.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class ReflectionUtilsTest {

    @Test
    void findComponentsClassLoaderCantBeNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ReflectionUtils.findComponents(null, "java.utils"));
    }

    @Test
    void getMethodsForObject() throws NoSuchMethodException {
        Set<Method> methods = ReflectionUtils.getMethods(Object.class);
        Assertions.assertTrue(methods.contains(Object.class.getDeclaredMethod("hashCode")));
        Assertions.assertTrue(methods.contains(Object.class.getDeclaredMethod("equals", Object.class)));
        Assertions.assertTrue(methods.contains(Object.class.getDeclaredMethod("clone")));
        Assertions.assertTrue(methods.contains(Object.class.getDeclaredMethod("getClass")));
    }

    @Test
    void getMethodsForNull() {
        Assertions.assertTrue(ReflectionUtils.getMethods(null).isEmpty());
    }

    @Test
    void getMethodAnnotationsForObject() throws NoSuchMethodException {
        Method method = StubClass.class.getDeclaredMethod("get");
        Assertions.assertTrue(ReflectionUtils.getMethodAnnotations(Object.class, method).isEmpty());
    }

    @Test
    void getMethodAnnotationsForNull() throws NoSuchMethodException {
        Method method = StubClass.class.getDeclaredMethod("get");
        Assertions.assertTrue(ReflectionUtils.getMethodAnnotations(null, method).isEmpty());
    }

    @Test
    void getMethodAnnotationsForInterface() throws NoSuchMethodException {
        Method method = StubInterface.class.getDeclaredMethod("get");
        Assertions.assertTrue(ReflectionUtils.getMethodAnnotations(StubInterface.class, method).isEmpty());
    }

    @Test
    void getMethodAnnotationsIgnorePrivateAnnotations() throws NoSuchMethodException {
        Method method = StubClass.class.getDeclaredMethod("get");
        Assertions.assertTrue(ReflectionUtils.getMethodAnnotations(StubClass.class, method).isEmpty());
    }

    @Test
    void getFieldAnnotationsForObject() throws NoSuchFieldException {
        Field field = StubClass.class.getDeclaredField("a");
        Assertions.assertTrue(ReflectionUtils.getFieldAnnotations(Object.class, field).isEmpty());
    }

    @Test
    void getFieldAnnotationsForNull() throws NoSuchFieldException {
        Field field = StubClass.class.getDeclaredField("a");
        Assertions.assertTrue(ReflectionUtils.getFieldAnnotations(null, field).isEmpty());
    }

    @Test
    void getFieldAnnotationsForInterface() throws NoSuchFieldException {
        Field field = StubInterface.class.getDeclaredField("a");
        Assertions.assertTrue(ReflectionUtils.getFieldAnnotations(StubInterface.class, field).isEmpty());
    }

    static class StubClass extends AStubClass {
        String a;

        public String get() {
            return a;
        }
    }

    static class AStubClass {
        private String get() {
            return "A";
        }

        public String get(String param) {
            return param;
        }
    }

    interface StubInterface {
        String a = "a";

        String get();
    }
}