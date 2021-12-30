package com.github.sibmaks.ti;

import lombok.Getter;
import lombok.ToString;
import com.github.sibmaks.ti.reflection.ClassInfo;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Component definition, contains info about component: name, real Java class and instance (not proxed)
 *
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@ToString
public class ComponentDefinition<T> {
    @Getter
    private final String name;
    @Getter
    private final ClassInfo<T> componentClass;
    @Getter
    private final T componentBaseInstance;
    @Getter
    private T componentInstance;
    private final Set<Object> marks;

    public ComponentDefinition(String name, ClassInfo<T> componentClass, T componentBaseInstance) {
        Objects.requireNonNull(name, "Component name can't be null");
        Objects.requireNonNull(componentClass, "Component class can't be null");
        Objects.requireNonNull(componentBaseInstance, "Component instance can't be null");
        this.name = name;
        this.componentClass = componentClass;
        this.componentBaseInstance = componentBaseInstance;
        this.componentInstance = componentBaseInstance;
        this.marks = new HashSet<>();
    }

    /**
     * Add mark to component definition
     * Return false in case if passed mark already exists
     * @param mark some not null mark
     * @return added or not
     */
    boolean mark(Object mark) {
        Objects.requireNonNull(mark, "Mark can't be null");
        return this.marks.add(mark);
    }

    /**
     * Change component current instance
     * @param componentInstance new instance
     */
    void setComponentInstance(T componentInstance) {
        this.componentInstance = componentInstance;
    }

    /**
     * Check is component definition has all passed marks or not
     * @param marks list of marks
     * @return true if contains all marks, false otherwise
     */
    public boolean isMarked(Object ... marks) {
        for (Object mark : marks) {
            if(!this.marks.contains(mark)) {
                return false;
            }
        }
        return true;
    }
}
