package cn.tisson.xmemcached.spring.boot.anno;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * cache expire time annotation
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
     * whether allow null values
     */
    boolean allowNullValues() default false;
}
