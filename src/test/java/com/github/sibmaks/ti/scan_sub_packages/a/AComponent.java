package com.github.sibmaks.ti.scan_sub_packages.a;

import com.github.sibmaks.ti.annotation.Component;
import com.github.sibmaks.ti.scan_sub_packages.b.BComponent;

import javax.inject.Inject;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component
public class AComponent {
    @Inject
    public BComponent bComponent;
}
