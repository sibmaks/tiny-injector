package com.github.sibmaks.ti.field_injection_list_as_component;

import com.github.sibmaks.ti.annotation.Component;

import javax.inject.Inject;
import java.util.List;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
class AComponent {
    @Inject
    public List<BComponent> bComponents;
}
