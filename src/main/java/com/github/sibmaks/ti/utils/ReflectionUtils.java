package com.github.sibmaks.ti.utils;

import com.github.sibmaks.ti.annotation.Component;
import com.github.sibmaks.ti.reflection.AnnotationInfo;
import com.github.sibmaks.ti.reflection.ClassInfo;
import com.github.sibmaks.ti.reflection.FieldInfo;
import com.github.sibmaks.ti.reflection.MethodInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Reflections utils for getting class in packages, get class annotations and so on.
 *
 * @author drobyshev-ma
 * Created at 18-08-2021
 */
@Slf4j
public class ReflectionUtils {
    private static final String CLASS_EXT = ".class";

    private ReflectionUtils() {

    }

    public static Map<String, ClassInfo<? super Object>> findComponents(ClassLoader classLoader, String packageName) throws IOException {
        Map<String, ClassInfo<? super Object>> components = new HashMap<>();
        Set<ClassInfo<? super Object>> classes = getClasses(classLoader, packageName, ReflectionUtils::isComponent);

        Set<ClassInfo<? super Object>> abstractClasses = new HashSet<>();
        Set<ClassInfo<? super Object>> implClasses = new HashSet<>();
        for(ClassInfo<? super Object> clazz : classes) {
            String name = getComponentName(clazz);
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
                Component annotation = (Component) componentAI.getAnnotation();
                String name = annotation.value();
                if ("".equals(name)) {
                    return StringUtils.toLowerCase1st(clazz.getSimpleName());
                }
                return name;
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
        Map<String, List<URL>> dirs = new HashMap<>();
        dirs.put("jar", new ArrayList<>());
        dirs.put("file", new ArrayList<>());
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            List<URL> files = dirs.get(resource.getProtocol());
            if(files == null) {
                log.warn("Protocol {} is unsupported", resource.getProtocol());
                continue;
            }
            files.add(resource);
        }
        Set<ClassInfo<? super Object>> classes = new HashSet<>();
        for (URL directory : dirs.get("file")) {
            classes.addAll(findClassesByFileProtocol(new File(directory.getFile()), packageName, condition));
        }
        for (URL directory : dirs.get("jar")) {
            classes.addAll(findClassesByJarProtocol(directory, condition));
        }
        return classes;
    }

    private static Set<ClassInfo<? super Object>> findClassesByJarProtocol(URL directory,
                                                                           Predicate<ClassInfo<? super Object>> condition) {
        String[] parts = directory.getFile().split("!");
        String jarFileName = parts[0];
        String innerPath = parts[1].substring(1);
        try {
            URL jarURL = new URL(jarFileName);
            File jarFile = new File(jarURL.getFile());
            if (!jarFile.exists()) {
                return Collections.emptySet();
            }
            Set<ClassInfo<? super Object>> classInfos = new HashSet<>();
            try (ZipFile zipFile = new ZipFile(jarURL.getFile())) {
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = entries.nextElement();
                    String name = zipEntry.getName();
                    if (!name.startsWith(innerPath) && name.endsWith(CLASS_EXT)) {
                        continue;
                    }
                    String className = name.substring(0, name.length() - CLASS_EXT.length()).replace("/", ".");
                    try {
                        Class<? super Object> clazz = (Class<? super Object>) Class.forName(className);
                        ClassInfo<? super Object> classInfo = ClassInfo.from(clazz);
                        if (condition.test(classInfo)) {
                            classInfos.add(classInfo);
                        }
                    } catch (ClassNotFoundException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            return classInfos;
        } catch (Exception e) {
            log.error("Get classes from jar exception", e);
        }
        return Collections.emptySet();
    }

    private static Set<ClassInfo<? super Object>> findClassesByFileProtocol(File directory, String packageName,
                                                                            Predicate<ClassInfo<? super Object>> condition) {
        if (!directory.exists()) {
            return Collections.emptySet();
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return Collections.emptySet();
        }
        Set<ClassInfo<? super Object>> classes = new HashSet<>();
        for (File file : files) {
            if (file.isDirectory()) {
                if (!file.getName().contains(".'")) {
                    classes.addAll(findClassesByFileProtocol(file, packageName + "." + file.getName(), condition));
                }
            } else if (file.getName().endsWith(CLASS_EXT)) {
                try {
                    Class<? super Object> clazz = (Class<? super Object>) Class.forName(packageName + '.' +
                            file.getName().substring(0, file.getName().length() - 6));
                    ClassInfo<? super Object> classInfo = ClassInfo.from(clazz);
                    if (condition.test(classInfo)) {
                        classes.add(classInfo);
                    }
                } catch (ClassNotFoundException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return classes;
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

    private static boolean isComponent(ClassInfo<? super Object> aClass) {
        return canBeComponent(aClass) &&
                aClass.getAnnotationInfos().stream().anyMatch(it -> it.isInherited(Component.class));
    }
}