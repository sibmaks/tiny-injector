package xyz.tiny.injector.interface_inheritance;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class InterfaceImpl implements BaseInterface {
    IIComponent component;

    @Override
    public void setValue(IIComponent component) {
        this.component = component;
    }

    @Override
    public IIComponent getValue() {
        return component;
    }
}
