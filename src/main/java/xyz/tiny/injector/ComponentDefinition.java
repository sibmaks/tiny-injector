package xyz.tiny.injector;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Component definition, contains info about component: name, real Java class and instance (not proxed)
 *
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@AllArgsConstructor
@Data
public class ComponentDefinition<T> {
    private final String name;
    private final Class<T> componentClass;
    private final T componentInstance;
}
