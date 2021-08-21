package xyz.tiny.injector.reflection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

/**
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
@EqualsAndHashCode
@ToString
public class FieldInfo {
    @Getter
    private final Field field;
    @Getter
    private final List<AnnotationInfo> annotationInfos;

    public FieldInfo(Field field, List<AnnotationInfo> annotationInfos) {
        this.field = field;
        this.annotationInfos = Collections.unmodifiableList(annotationInfos);
    }

    public boolean isFinal() {
        return Modifier.isFinal(field.getModifiers());
    }

    public String getName() {
        return field.getName();
    }

    public void set(Object source, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(source, value);
        field.setAccessible(false);
    }
}
