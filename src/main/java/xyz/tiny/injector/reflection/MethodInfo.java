package xyz.tiny.injector.reflection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import xyz.tiny.injector.exception.MethodInjectionException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
@EqualsAndHashCode
@ToString
public class MethodInfo {
    @Getter
    private final Method method;
    @Getter
    private final List<AnnotationInfo> annotationInfos;

    public MethodInfo(Method method, List<AnnotationInfo> annotationInfos) {
        this.method = method;
        this.annotationInfos = Collections.unmodifiableList(annotationInfos);
    }

    public boolean isPrivate() {
        return Modifier.isPrivate(method.getModifiers());
    }

    public boolean same(MethodInfo methodInfo) {
        Method thisMethod = method;
        Method checkMethod = methodInfo.method;
        return checkMethod.getDeclaringClass().isAssignableFrom(thisMethod.getDeclaringClass()) &&
                thisMethod.getName().equals(checkMethod.getName()) &&
                Arrays.equals(thisMethod.getParameterTypes(), checkMethod.getParameterTypes());
    }

    public String getName() {
        return method.getName();
    }

    public void invoke(Object source, Object ... args) {
        method.setAccessible(true);
        try {
            method.invoke(source, args);
        } catch (Throwable throwable) {
            throw new MethodInjectionException(throwable);
        }
        method.setAccessible(false);
    }

    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    public boolean isBridge() {
        return method.isBridge();
    }

    public boolean isSynthetic() {
        return method.isSynthetic();
    }
}
