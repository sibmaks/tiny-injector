package xyz.tiny.injector;

import lombok.Getter;
import lombok.ToString;
import xyz.tiny.injector.reflection.ClassInfo;

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
    private final IComponentDefinitionObserver componentDefinitionObserver;

    public ComponentDefinition(String name, ClassInfo<T> componentClass, T componentBaseInstance,
                               IComponentDefinitionObserver componentDefinitionObserver) {
        Objects.requireNonNull(name, "Component name can't be null");
        Objects.requireNonNull(componentClass, "Component class can't be null");
        Objects.requireNonNull(componentBaseInstance, "Component instance can't be null");
        this.name = name;
        this.componentClass = componentClass;
        this.componentBaseInstance = componentBaseInstance;
        this.componentInstance = componentBaseInstance;
        this.componentDefinitionObserver = componentDefinitionObserver;
        this.marks = new HashSet<>();
    }

    /**
     * Add mark to component definition
     * Return false in case if passed mark already exists
     * @param mark some not null mark
     * @return added or not
     * @throws Exception
     */
    public boolean mark(Object mark) throws Exception {
        Objects.requireNonNull(mark, "Mark can't be null");
        if(!this.marks.contains(mark) && this.marks.add(mark) && componentDefinitionObserver != null) {
            componentDefinitionObserver.onMarkAdded(this);
            return true;
        }
        return false;
    }

    void setComponentInstance(T componentInstance) throws Exception {
        this.componentInstance = componentInstance;
        if(componentDefinitionObserver != null) {
            componentDefinitionObserver.onInstanceChanged(this);
        }
    }

    public boolean isMarked(Object ... marks) {
        for (Object mark : marks) {
            if(!this.marks.contains(mark)) {
                return false;
            }
        }
        return true;
    }
}
