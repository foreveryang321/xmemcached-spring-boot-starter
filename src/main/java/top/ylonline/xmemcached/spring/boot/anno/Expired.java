package top.ylonline.xmemcached.spring.boot.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义缓存过期时间配置注解，过期时间单位：s（秒）
 * <p>
 * 如果要使用 &#64;{@link Expired}注解，需要启用 &#64;{@link EnableXMemcachedConfiguration}
 *
 * @author YL
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Expired {
    /**
     * 过期时间，默认：-1，使用全局默认过期时间
     * <pre>
     *     与 {@link #el()} 属性互斥. 优先使用 {@link #el()} 配置。
     * </pre>
     */
    int value() default -1;

    /**
     * Spring Expression Language (SpEL) expression for computing the expire time dynamically.
     * <pre>
     *     与 {@link #value()} 属性互斥. 优先使用当前配置。
     *     由于该实现是重新构建一个cacheName，这样会导致配合CachePut、CacheEvict等使用时会有问题。
     * </pre>
     */
    String el() default "";
}
