package com.github.sibmaks.ti.utils;

import com.github.sibmaks.ti.utils.ReflectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.github.sibmaks.ti.reflection.MethodInfo;

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
    void getMethodsForObject() {
        Set<MethodInfo> methods = ReflectionUtils.getMethods(Object.class);
        Assertions.assertTrue(methods.stream().anyMatch(it -> isSame(it.getMethod(), "hashCode")));
        Assertions.assertTrue(methods.stream().anyMatch(it -> isSame(it.getMethod(), "equals", Object.class)));
        Assertions.assertTrue(methods.stream().anyMatch(it -> isSame(it.getMethod(), "clone")));
        Assertions.assertTrue(methods.stream().anyMatch(it -> isSame(it.getMethod(), "getClass")));
    }

    private boolean isSame(Method method, String objectMethodName, Class ... params) {
        try {
            return method.equals(Object.class.getDeclaredMethod(objectMethodName, params));
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    @Test
    void getMethodsForNull() {
        Assertions.assertTrue(ReflectionUtils.getMethods(null).isEmpty());
    }
}