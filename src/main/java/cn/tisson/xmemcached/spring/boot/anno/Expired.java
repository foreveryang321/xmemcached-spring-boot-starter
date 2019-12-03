package cn.tisson.xmemcached.spring.boot.anno;

import org.springframework.core.annotation.AliasFor;

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
     * expire time, default 60s.
     */
    @AliasFor("expire")
    int value() default 60;

    /**
     * expire time, default 60S.
     */
    @AliasFor("value")
    int expire() default 60;

    /**
     * Spring Expression Language (SpEL) expression for computing the expire time dynamically.
     *
     * <p>
     * 与 {@link #value()} 属性互斥. 使用该属性配置的过期时间优先级比 {@link #value()} 属性高。由于该实现是重新构建一个cacheName，这样会导致配合CachePut
     * 、CacheEvict等使用时会有问题。
     * </p>
     */
    String spEl() default "";
}
