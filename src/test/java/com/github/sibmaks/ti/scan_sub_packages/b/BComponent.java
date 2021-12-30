package com.github.sibmaks.ti.scan_sub_packages.b;

import com.github.sibmaks.ti.annotation.Component;
import com.github.sibmaks.ti.scan_sub_packages.a.AComponent;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 20-08-2021
 */
@Component
public class BComponent {
    @Inject
    public AComponent aComponent;
}
