# Spring & Spring Boot Internals: The Senior Architect Guide

**Target Audience:** Senior Java Backend Developers (~5 Years Experience)  
**Topic:** Spring Framework Core, Bean Lifecycle, IoC/DI Mechanics, AOP Proxies, and Auto-Configuration  

---

## 1. Inversion of Control (IoC) & The Bean Lifecycle

### Core Interfaces:
- `BeanFactory`: The fundamental root interface providing basic dependency injection (`getBean()`). Performs lazy initialization.
- `ApplicationContext`: Extends `BeanFactory`. Adds enterprise capabilities: eager singleton instantiation, event publication (`ApplicationEventPublisher`), message internationalization (`MessageSource`), and integration with AOP.

```text
1. Instantiation (Constructor called via Reflection / CGLIB)
      │
      ▼
2. Populate Properties (Dependency Injection via setters/fields)
      │
      ▼
3. BeanNameAware / BeanFactoryAware / ApplicationContextAware
      │
      ▼
4. BeanPostProcessor#postProcessBeforeInitialization()
      │
      ▼
5. Initialization Callbacks:
   - @PostConstruct
   - InitializingBean#afterPropertiesSet()
   - custom initMethod
      │
      ▼
6. BeanPostProcessor#postProcessAfterInitialization()
   (*** CRITICAL: This is where Spring wraps the bean in an AOP Proxy for @Transactional / @Async! ***)
      │
      ▼
7. Bean Ready for Production Use
      │
      ▼
8. Destruction Callbacks:
   - @PreDestroy
   - DisposableBean#destroy()
   - custom destroyMethod
```

---

## 2. Spring AOP: JDK Dynamic Proxy vs. CGLIB

Spring AOP uses **runtime dynamic proxies** rather than compile-time bytecode manipulation (like AspectJ).

| Feature | JDK Dynamic Proxy | CGLIB Proxy |
|---|---|---|
| **Mechanism** | Standard Java reflection (`java.lang.reflect.Proxy`) | Bytecode generation library (`org.springframework.cglib`) |
| **Requirement** | Target class must implement an **interface** | Generates a dynamic **subclass** of the target bean |
| **Methods Proxied** | Only methods declared in the interface | All non-final, non-private methods |
| **Spring Boot 3 Default** | Opt-in | **Default proxying mechanism** (`spring.aop.proxy-target-class=true`) |

### The "Self-Invocation" Pitfall in `@Transactional` (High-Frequency Interview Question)
```java
@Service
public class OrderService {

    public void processOrder(Long orderId) {
        // Internal call to another method in the same class:
        updateInventory(orderId);
    }

    @Transactional
    public void updateInventory(Long orderId) {
        // DB operations...
    }
}
```
**Why does `@Transactional` FAIL to start a transaction here?**
- When an external caller invokes `orderService.processOrder()`, it calls the method on the **Spring CGLIB Proxy**.
- Inside `processOrder()`, calling `updateInventory()` executes `this.updateInventory()` directly on the **target instance**, completely bypassing the proxy!
- Because the proxy is bypassed, the `TransactionInterceptor` never executes, and no transaction is opened.
- *Solutions:*
  1. Move `updateInventory()` into a separate service class.
  2. Inject `OrderService` into itself (self-injection) and call `self.updateInventory()`.
  3. Use `TransactionTemplate` programmatic transactions.

---

## 3. How Spring Boot Auto-Configuration Works Internally

1. `@SpringBootApplication` is a meta-annotation composed of:
   - `@SpringBootConfiguration`: Marks class as a configuration source.
   - `@ComponentScan`: Scans components within current package.
   - `@EnableAutoConfiguration`: Activates Spring Boot auto-configuration mechanism.
2. Under the hood, `@EnableAutoConfiguration` imports `AutoConfigurationImportSelector`.
3. In Spring Boot 3+, it reads configuration class names from:
   `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
4. Each auto-configuration class uses conditional annotations to decide whether to activate:
   - `@ConditionalOnClass(DataSource.class)`: Only load if the class is on the classpath.
   - `@ConditionalOnMissingBean(DataSource.class)`: Only create bean if the user hasn't defined their own custom bean.
   - `@ConditionalOnProperty(name = "feature.enabled", havingValue = "true")`.
