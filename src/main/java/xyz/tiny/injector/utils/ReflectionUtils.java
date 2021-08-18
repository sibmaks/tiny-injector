package xyz.tiny.injector.utils;

import lombok.extern.slf4j.Slf4j;
import xyz.tiny.injector.annotation.Component;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;

/**
 * Reflections utils for getting class in packages, get class annotations and so on.
 *
 * @author drobyshev-ma
 * Created at 18-08-2021
 */
@Slf4j
public class ReflectionUtils {
    private static final Map<Class<?>, List<Annotation>> annotationsCache = new ConcurrentHashMap<>();

    private ReflectionUtils() {

    }

    public static Map<String, Class<?>> findComponents(ClassLoader classLoader, String packageName) throws IOException {
        Map<String, Class<?>> components = new HashMap<>();
        Set<Class<?>> classes = getClasses(classLoader, packageName, aClass -> getAnnotations(aClass).stream()
                .anyMatch(it -> it.annotationType() == Component.class));

        for(Class<?> clazz : classes) {
            String name = getComponentName(clazz);
            if("".equals(name)) {
                char[] simpleName = clazz.getSimpleName().toCharArray();
                simpleName[0] += 32;
                name = new String(simpleName);
            }
            Class<?> aClass = components.get(name);
            if(aClass != null) {
                if(aClass.isInterface() && aClass.isAssignableFrom(clazz)) {
                    components.put(name, clazz);
                } else if (!clazz.isInterface() || !clazz.isAssignableFrom(aClass)) {
                    throw new IllegalStateException(String.format("Duplicate component name: %s for classes: %s, %s",
                            name, aClass.getName(), clazz.getName()));
                }
            } else {
                components.put(name, clazz);
            }
        }
        return components;
    }

    private static String getComponentName(Class<?> clazz) {
        List<Annotation> annotations = getAnnotations(clazz);
        for(int i = annotations.size() - 1; i >= 0; i--) {
            Annotation annotation = annotations.get(i);
            if(annotation.annotationType() == Component.class) {
                return ((Component) annotation).value();
            }
        }
        return "";
    }

    private static List<Annotation> getAnnotations(Annotation annotation) {
        Annotation[] annotations = annotation.annotationType().getDeclaredAnnotations();
        if(annotations.length == 0) {
            return Collections.emptyList();
        }
        List<Annotation> subAnnotations = new ArrayList<>();
        getAnnotations(annotation, subAnnotations);
        return subAnnotations;
    }

    private static void getAnnotations(Annotation annotation, List<Annotation> result) {
        Annotation[] annotations = annotation.annotationType().getDeclaredAnnotations();
        if(annotations.length == 0) {
            return;
        }
        for (Annotation subAnnotation : annotations) {
            if(!result.contains(subAnnotation)) {
                result.add(subAnnotation);
                getAnnotations(subAnnotation, result);
                result.remove(subAnnotation);
                result.add(subAnnotation);
            }
        }
    }

    private static List<Annotation> getAnnotations(Class<?> clazz) {
        if(clazz == null) {
            throw new IllegalArgumentException("Class can't be null");
        }
        return annotationsCache.computeIfAbsent(clazz, it -> {
            if(it == Object.class) {
                return Collections.emptyList();
            }

            List<Annotation> annotations = new ArrayList<>();

            for (Class<?> anInterface : it.getInterfaces()) {
                for (Annotation annotation : getAnnotations(anInterface)) {
                    annotations.addAll(getAnnotations(annotation));
                    annotations.add(annotation);
                }
            }

            Class<?> superclass = it.getSuperclass();
            if(superclass != null) {
                for (Annotation annotation : getAnnotations(superclass)) {
                    annotations.addAll(getAnnotations(annotation));
                    annotations.add(annotation);
                }
            }

            for (Annotation annotation : it.getDeclaredAnnotations()) {
                annotations.addAll(getAnnotations(annotation));
                annotations.add(annotation);
            }

            if(!annotations.isEmpty()) {
                log.trace("Class {} contains annotations: {}", it, annotations);
            } else {
                log.trace("Class {} not contains annotations", it);
            }

            return annotations;
        });
    }

    private static Set<Class<?>> getClasses(ClassLoader classLoader, String packageName, Predicate<Class<?>> condition)
            throws IOException {
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        Set<Class<?>> classes = new HashSet<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName, condition));
        }
        return classes;
    }

    private static Set<Class<?>> findClasses(File directory, String packageName, Predicate<Class<?>> condition) {
        if (!directory.exists()) {
            return Collections.emptySet();
        }
        File[] files = directory.listFiles();
        if(files != null) {
            Set<Class<?>> classes = new HashSet<>();
            for (File file : files) {
                if (file.isDirectory()) {
                    if(!file.getName().contains(".'")) {
                        classes.addAll(findClasses(file, packageName + "." + file.getName(), condition));
                    }
                } else if (file.getName().endsWith(".class")) {
                    try {
                        Class<?> clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                        if(condition.test(clazz)) {
                            classes.add(clazz);
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
    public static Set<Field> getFields(Class<?> clazz) {
        if(clazz == null || clazz == Object.class) {
            return Collections.emptySet();
        }
        Set<Field> fields = new HashSet<>(Arrays.asList(clazz.getDeclaredFields()));
        fields.addAll(getFields(clazz.getSuperclass()));
        return fields;
    }

    /**
     * Get all methods, that class contains and inheritance
     * In case of null value return empty set
     * @param clazz some Java class
     * @return set of methods or empty set
     */
    public static Set<Method> getMethods(Class<?> clazz) {
        if(clazz == null || clazz == Object.class) {
            return Collections.emptySet();
        }
        Set<Method> methods = new HashSet<>(Arrays.asList(clazz.getDeclaredMethods()));
        methods.addAll(getMethods(clazz.getSuperclass()));
        return methods;
    }
}