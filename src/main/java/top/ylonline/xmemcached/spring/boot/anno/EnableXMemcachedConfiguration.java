package top.ylonline.xmemcached.spring.boot.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * enable redis auto configuration
 * <pre>
 *     主要注册 RedisTemplate、KeyGenerator、CacheManager
 * </pre>
 *
 * @author YL
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EnableXMemcachedConfiguration {
}
