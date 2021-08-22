# Tiny Injector

Simple Java dependency injection library

Actually, this is an educational project, so if you want to use it anywhere think before twice.

And if you have some comments or recommendation fill free to send it.

Support:
- field injection
- method injection
- 2nd level constructor
- provided values
- component customization (for example proxy)

# Dependencies

Library targeted to minimal using of external dependencies, so this library depends only on **javax.inject** library and also use **slf4j** for logging.

For code generation used **Lombok**.

# Using example

Any class what has to be processed by library should be annotated by this annotation:
```java
xyz.tiny.injector.annotation.Component
```

To execute injection you should call any of `buildInjections` methods in `xyz.tiny.injector.Injector`. 

By default, injector used class loader of current thread.

The return type is `IContext` interface, which contains all created components and their definitions.

For example
```java

import xyz.tiny.injector.Injector;
import xyz.tiny.injector.context.IContext;

public class Application {
    public static void main(String ... args) throws Throwable {
        IContext context = Injector.buildInjections(Application.class.getPackage().getName());
        AnyComponent anyComponent = context.getComponent("anyComponent");
        anyComponent.callMethod();
        // and so on
    }
}
```

## Field injection
Requirements:
- A field in component should be annotated by `javax.inject.Inject`
- 
```java
// in SomeComponent.java
import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;

@Component
public class SomeComponent {
    @Inject
    private AnotherComponent anotherComponent;
}

// in AnotherComponent.java
import xyz.tiny.injector.annotation.Component;

@Component
public class AnotherComponent {
}
```

## Method injection
Requirements:
- A method in component should be annotated by `javax.inject.Inject`

For name chaining `javax.inject.Named` annotation can be used.

```java
// in SomeComponent.java
import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;
import javax.inject.Named;

@Component
public class SomeComponent {
    @Inject
    void set(AnotherComponent param) {
      // This method can be called more than once, if some customizers decide to change injected value
    }
    
    @Inject
    void set2Values(@Named("instnace1") AnotherComponent param,
                    @Named("instance2") AnotherComponent param2) {
      // This method can be called more than once, if some customizers decide to change injected value
    }
}

// in AnotherComponent.java
import xyz.tiny.injector.annotation.Component;

@Component
public class AnotherComponent {
}
```

## 2nd level constructor

Called once after all context fully initialized.

Requirements:
- A method in component should be annotated by `xyz.tiny.injector.annotation.PostInitialization`
- Annotated method should not receive any parameters

```java
// in SomeComponent.java
import xyz.tiny.injector.annotation.Component;

import javax.inject.Inject;

@Component
public class SomeComponent {
    @PostInitialization
    void init() {
    
    }
}
```

## Using providers

Example can be found here: [provider test](https://github.com/sibmaks/tiny-injector/tree/main/src/test/java/xyz/tiny/injector/provide_component).

## Component customization (for example proxy)

In this example **cglib** used

```java
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import xyz.tiny.injector.ComponentDefinition;
import xyz.tiny.injector.annotation.Component;
import xyz.tiny.injector.context.IMutableContext;
import xyz.tiny.injector.context.listener.IContextListener;

@Component
public class ProxyWrapper implements IContextListener {
    @Override
    public void onAddComponentDefinition(ComponentDefinition<?> componentDefinition, IMutableContext context) throws Exception {
        Object instance = componentDefinition.getComponentInstance();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(componentDefinition.getComponentClass().get());
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            // TODO: some additional proxy logic
            return proxy.invoke(instance, args);
        });
        context.update(componentDefinition.getName(), enhancer.create());
    }
}
```
