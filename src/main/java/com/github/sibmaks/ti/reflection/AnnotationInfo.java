package com.github.sibmaks.ti.reflection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
@EqualsAndHashCode
@ToString
@Slf4j
public class AnnotationInfo {
    @Getter
    private final Annotation annotation;
    @Getter
    private final List<AnnotationInfo> annotationInfos;

    private AnnotationInfo(Annotation annotation, List<AnnotationInfo> annotationInfos) {
        this.annotation = annotation;
        this.annotationInfos = Collections.unmodifiableList(annotationInfos);
    }

    public AnnotationInfo getInherited(Class<? extends Annotation> clazz) {
        if(this.annotation.annotationType() == clazz) {
            return this;
        }
        for (AnnotationInfo annotationInfo : annotationInfos) {
            AnnotationInfo annotationInfoInherited = annotationInfo.getInherited(clazz);
            if(annotationInfoInherited != null) {
                return annotationInfoInherited;
            }
        }
        return null;
    }

    public boolean isInherited(Class<? extends Annotation> clazz) {
        if(this.annotation.annotationType() == clazz) {
            return true;
        }
        for (AnnotationInfo annotationInfo : annotationInfos) {
            if(annotationInfo.isInherited(clazz)) {
                return true;
            }
        }
        return false;
    }

    public static AnnotationInfo from(Annotation annotation) {
        List<AnnotationInfo> subAnnotations = new ArrayList<>();
        getAnnotations(annotation, subAnnotations);
        return new AnnotationInfo(annotation, subAnnotations);
    }

    public static List<AnnotationInfo> forClass(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class can't be null");
        }
        if (clazz == Object.class) {
            return Collections.emptyList();
        }
        List<AnnotationInfo> annotations = new ArrayList<>();

        for (Class<?> anInterface : clazz.getInterfaces()) {
            annotations.addAll(AnnotationInfo.forClass(anInterface));
        }

        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            annotations.addAll(AnnotationInfo.forClass(superclass));
        }

        for (Annotation annotation : clazz.getDeclaredAnnotations()) {
            AnnotationInfo annotationInfo = AnnotationInfo.from(annotation);
            annotations.add(annotationInfo);
        }

        if (!annotations.isEmpty()) {
            log.trace("Class {} contains annotations: {}", clazz, annotations);
        } else {
            log.trace("Class {} not contains annotations", clazz);
        }

        Collections.reverse(annotations);
        return annotations;
    }

    private static void getAnnotations(Annotation annotation, List<AnnotationInfo> result) {
        Annotation[] annotations = annotation.annotationType().getDeclaredAnnotations();
        if(annotations.length == 0) {
            return;
        }
        for (Annotation subAnnotation : annotations) {
            if(!isAnnotationInherited(subAnnotation, result)) {
                AnnotationInfo annotationInfo = new AnnotationInfo(subAnnotation, Collections.emptyList());
                List<AnnotationInfo> wasAnnotations = new ArrayList<>(result);
                // Add current annotations to list with empty parent annotations
                result.add(annotationInfo);
                getAnnotations(subAnnotation, result);

                // Get parent annotations for sub annotation
                List<AnnotationInfo> newAnnotations = result.stream().filter(it -> !wasAnnotations.contains(it)).collect(Collectors.toList());
                result.removeAll(newAnnotations);

                result.remove(annotationInfo);
                annotationInfo = new AnnotationInfo(subAnnotation, newAnnotations);
                result.add(annotationInfo);
            }
        }
    }

    private static boolean isAnnotationInherited(Annotation annotation, List<AnnotationInfo> inheritedAnnotations) {
        for (AnnotationInfo inheritedAnnotation : inheritedAnnotations) {
            if(inheritedAnnotation.annotation == annotation) {
                return true;
            }
            if(isAnnotationInherited(annotation, inheritedAnnotation.annotationInfos)) {
                return true;
            }
        }
        return false;
    }

    public static List<AnnotationInfo> forField(Field field) {
        Class<?> clazz = field.getDeclaringClass();
        if(clazz == Object.class) {
            return Collections.emptyList();
        }
        List<AnnotationInfo> annotations = new ArrayList<>();
        Class<?> superclass = clazz.getSuperclass();
        if(superclass != null && superclass != Object.class) {
            for (Field declaredField : superclass.getDeclaredFields()) {
                if(!Modifier.isPrivate(declaredField.getModifiers()) &&
                        declaredField.getName().equals(field.getName())) {
                    annotations.addAll(forField(declaredField));
                    break;
                }
            }
        }

        List<AnnotationInfo> infoList = Arrays.stream(field.getDeclaredAnnotations())
                .map(AnnotationInfo::from)
                .collect(Collectors.toList());
        annotations.addAll(infoList);
        Collections.reverse(annotations);
        return annotations;
    }

    public static List<AnnotationInfo> forMethod(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        if(clazz == Object.class) {
            return Collections.emptyList();
        }
        List<AnnotationInfo> annotations = new ArrayList<>();
        Class<?> superclass = clazz.getSuperclass();
        if(superclass != null && superclass != Object.class) {
            getParentAnnotations(method, annotations, superclass);
        }

        for (Class<?> anInterface : clazz.getInterfaces()) {
            getParentAnnotations(method, annotations, anInterface);
        }

        List<AnnotationInfo> infoList = Arrays.stream(method.getDeclaredAnnotations())
                .map(AnnotationInfo::from)
                .collect(Collectors.toList());
        annotations.addAll(infoList);
        Collections.reverse(annotations);
        return annotations;
    }

    private static void getParentAnnotations(Method method, List<AnnotationInfo> annotations, Class<?> anInterface) {
        for (Method declaredMethod : anInterface.getDeclaredMethods()) {
            if(!Modifier.isPrivate(declaredMethod.getModifiers()) &&
                    !declaredMethod.isBridge() && !declaredMethod.isSynthetic() &&
                    declaredMethod.getName().equals(method.getName()) &&
                    Arrays.equals(declaredMethod.getParameterTypes(), method.getParameterTypes())) {
                annotations.addAll(forMethod(declaredMethod));
                break;
            }
        }
    }
}
