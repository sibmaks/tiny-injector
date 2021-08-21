package xyz.tiny.injector.field_injection_list_as_component;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Named;
import javax.inject.Provider;
import java.util.Arrays;
import java.util.List;

/**
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
@Component
public class BListProvider implements Provider<List<BComponent>> {
    @Override
    @Named("bComponents")
    public List<BComponent> get() {
        return Arrays.asList(new BComponent());
    }
}
