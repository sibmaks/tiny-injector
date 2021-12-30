package com.github.sibmaks.ti.reflection;

import com.github.sibmaks.ti.utils.ReflectionUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
@EqualsAndHashCode
@ToString
public class ClassInfo<T> {
    private final Class<T> clazz;
    @Getter
    private final List<AnnotationInfo> annotationInfos;
    @Getter
    private final Set<MethodInfo> methodInfos;
    @Getter
    private final Set<FieldInfo> fieldInfos;

    private ClassInfo(Class<T> clazz, List<AnnotationInfo> annotationInfos, Set<MethodInfo> methodInfos,
                      Set<FieldInfo> fieldInfos) {
        this.clazz = clazz;
        this.annotationInfos = Collections.unmodifiableList(annotationInfos);
        this.methodInfos = Collections.unmodifiableSet(methodInfos);
        this.fieldInfos = Collections.unmodifiableSet(fieldInfos);
    }

    public Class<T> get() {
        return clazz;
    }

    public boolean isInterface() {
        return clazz.isInterface();
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    public boolean isAssignableFrom(ClassInfo<?> classInfo) {
        return this.clazz.isAssignableFrom(classInfo.clazz);
    }

    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    public String getName() {
        return clazz.getName();
    }

    public String getPackageName() {
        return clazz.getPackage().getName();
    }

    public static<T> ClassInfo<T> from(Class<T> clazz) {
        return new ClassInfo<>(clazz, AnnotationInfo.forClass(clazz), ReflectionUtils.getMethods(clazz),
                ReflectionUtils.getFields(clazz));
    }
}
