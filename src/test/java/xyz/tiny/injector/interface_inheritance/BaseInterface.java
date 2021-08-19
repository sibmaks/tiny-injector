package xyz.tiny.injector.interface_inheritance;

import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
@Component("interface")
interface BaseInterface {

    @Inject
    @Named("iIComponent")
    void setValue(IIComponent component);

    IIComponent getValue();
}
