package com.github.sibmaks.ti.reflection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

/**
 * @author drobyshev-ma
 * Created at 22-08-2021
 */
class AnnotationInfoTest {
    @Test
    public void annotationForNullClassException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> AnnotationInfo.forClass(null));
    }

    @Test
    public void fieldAnnotationForObjectIsEmpty() throws NoSuchFieldException, IllegalAccessException {
        Field field = AComponent.class.getDeclaredField("bComponent");

        Field clazzField = field.getClass().getDeclaredField("clazz");
        clazzField.setAccessible(true);
        clazzField.set(field, Object.class);

        Assertions.assertTrue(AnnotationInfo.forField(field).isEmpty());
    }
}