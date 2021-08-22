package xyz.tiny.injector.utils;

import lombok.extern.slf4j.Slf4j;
import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.reflection.AnnotationInfo;
import xyz.tiny.injector.reflection.ClassInfo;
import xyz.tiny.injector.reflection.FieldInfo;
import xyz.tiny.injector.reflection.MethodInfo;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Reflections utils for getting class in packages, get class annotations and so on.
 *
 * @author drobyshev-ma
 * Created at 18-08-2021
 */
@Slf4j
public class ReflectionUtils {
    private ReflectionUtils() {

    }

    public static Map<String, ClassInfo<? super Object>> findComponents(ClassLoader classLoader, String packageName) throws IOException {
        Map<String, ClassInfo<? super Object>> components = new HashMap<>();
        Set<ClassInfo<? super Object>> classes = getClasses(classLoader, packageName, aClass -> canBeComponent(aClass) &&
                aClass.getAnnotationInfos().stream().anyMatch(it -> it.isInherited(Component.class)));

        Set<ClassInfo<? super Object>> abstractClasses = new HashSet<>();
        Set<ClassInfo<? super Object>> implClasses = new HashSet<>();
        for(ClassInfo<? super Object> clazz : classes) {
            String name = getComponentName(clazz);
            if ("".equals(name)) {
                name = StringUtils.toLowerCase1st(clazz.getSimpleName());
            }
            if(clazz.isInterface() || clazz.isAbstract()) {
                if(implClasses.stream().noneMatch(clazz::isAssignableFrom)) {
                    abstractClasses.add(clazz);
                }
            } else {
                ClassInfo<? super Object> aClass = components.get(name);
                if (aClass != null) {
                    throw new IllegalStateException(String.format("Duplicate component name: %s, classes: %s, %s",
                            name, aClass.getName(), clazz.getName()));
                } else {
                    components.put(name, clazz);
                    implClasses.add(clazz);
                    abstractClasses.removeAll(abstractClasses.stream()
                            .filter(it -> it.isAssignableFrom(clazz))
                            .collect(Collectors.toSet()));
                }
            }
        }
        implClasses.clear();
        if(!abstractClasses.isEmpty()) {
            throw new IllegalStateException(String.format("Component without implementation: %s", abstractClasses));
        }
        return components;
    }

    private static boolean canBeComponent(ClassInfo<?> classInfo) {
        Class<?> clazz = classInfo.get();
        return !clazz.isAnnotation() && !clazz.isAnonymousClass() && !clazz.isPrimitive() && !clazz.isSynthetic() &&
                !clazz.isEnum() && !clazz.isArray();
    }

    private static String getComponentName(ClassInfo<?> clazz) {
        for(AnnotationInfo annotationInfo : clazz.getAnnotationInfos()) {
            AnnotationInfo componentAI = annotationInfo.getInherited(Component.class);
            if(componentAI != null) {
                Annotation annotation = componentAI.getAnnotation();
                return ((Component) annotation).value();
            }
        }
        throw new IllegalStateException(String.format("Class %s is not component", clazz.getName()));
    }

    private static Set<ClassInfo<? super Object>> getClasses(ClassLoader classLoader, String packageName,
                                                             Predicate<ClassInfo<? super Object>> condition)
            throws IOException {
        if(classLoader == null) {
            throw new IllegalArgumentException("Class loader can't be null");
        }
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        Set<ClassInfo<? super Object>> classes = new HashSet<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName, condition));
        }
        return classes;
    }

    private static Set<ClassInfo<? super Object>> findClasses(File directory, String packageName,
                                                              Predicate<ClassInfo<? super Object>> condition) {
        if (!directory.exists()) {
            return Collections.emptySet();
        }
        File[] files = directory.listFiles();
        if(files != null) {
            Set<ClassInfo<? super Object>> classes = new HashSet<>();
            for (File file : files) {
                if (file.isDirectory()) {
                    if(!file.getName().contains(".'")) {
                        classes.addAll(findClasses(file, packageName + "." + file.getName(), condition));
                    }
                } else if (file.getName().endsWith(".class")) {
                    try {
                        Class<? super Object> clazz = (Class<? super Object>) Class.forName(packageName + '.' +
                                file.getName().substring(0, file.getName().length() - 6));
                        ClassInfo<? super Object> classInfo = ClassInfo.from(clazz);
                        if(condition.test(classInfo)) {
                            classes.add(classInfo);
                        }
                    } catch (ClassNotFoundException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            return classes;
        }
        return Collections.emptySet();
    }


    /**
     * Get all fields, that class contains and inheritance
     * In case of null value return empty set
     * @param clazz some Java class
     * @return set of fields or empty set
     */
    public static Set<FieldInfo> getFields(Class<?> clazz) {
        if(clazz == null) {
            return Collections.emptySet();
        }
        Set<FieldInfo> fields = Arrays
                .stream(clazz.getDeclaredFields())
                .map(it -> new FieldInfo(it, AnnotationInfo.forField(it)))
                .collect(Collectors.toSet());
        fields.addAll(getFields(clazz.getSuperclass()));
        return fields;
    }

    /**
     * Get all methods, that class contains and inheritance
     * In case of null value return empty set
     * @param clazz some Java class
     * @return set of methods or empty set
     */
    public static Set<MethodInfo> getMethods(Class<?> clazz) {
        if(clazz == null) {
            return Collections.emptySet();
        }
        Set<MethodInfo> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(it -> !it.isSynthetic() && !it.isBridge())
                .map(it -> new MethodInfo(it, AnnotationInfo.forMethod(it)))
                .collect(Collectors.toSet());
        for (MethodInfo method : getMethods(clazz.getSuperclass())) {
            if(!method.isBridge() && !method.isSynthetic() && (method.isPrivate() || methods.stream().noneMatch(it -> it.same(method)))) {
                methods.add(method);
            }
        }
        return methods;
    }
}