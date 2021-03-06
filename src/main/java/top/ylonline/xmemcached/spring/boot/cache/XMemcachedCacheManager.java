package top.ylonline.xmemcached.spring.boot.cache;

import top.ylonline.xmemcached.spring.boot.anno.Expired;
import top.ylonline.xmemcached.spring.boot.util.CacheUtils;
import lombok.extern.slf4j.Slf4j;
import net.rubyeye.xmemcached.MemcachedClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author YL
 */
@Slf4j
public class XMemcachedCacheManager extends AbstractTransactionSupportingCacheManager
        implements ApplicationContextAware, InitializingBean {
    private MemcachedClient memcachedClient;
    private int grobalExpire;
    private boolean allowNullValues;
    private Map<String, XMemcachedCacheProperties> initialCacheConfiguration = new LinkedHashMap<>();

    private ApplicationContext applicationContext;

    public XMemcachedCacheManager(MemcachedClient memcachedClient, int grobalExpire, boolean allowNullValues) {
        this.memcachedClient = memcachedClient;
        this.grobalExpire = grobalExpire;
        this.allowNullValues = allowNullValues;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        for (String beanName : beanNames) {
            final Class<?> clazz = applicationContext.getType(beanName);
            doWith(clazz);
        }
        super.afterPropertiesSet();
    }

    @Override
    protected Collection<XMemcachedCache> loadCaches() {
        List<XMemcachedCache> caches = new LinkedList<>();
        for (Map.Entry<String, XMemcachedCacheProperties> entry : initialCacheConfiguration.entrySet()) {
            caches.add(createCache(entry.getKey(), entry.getValue()));
        }
        return caches;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = super.getCache(name);
        return new XMemcachedCacheWrapper(cache);
    }

    @Override
    protected XMemcachedCache getMissingCache(String name) {
        XMemcachedCacheProperties properties = new XMemcachedCacheProperties(
                CacheUtils.computeTtl(name, this.grobalExpire),
                this.allowNullValues
        );
        return this.createCache(name, properties);
    }

    /**
     * Configuration hook for creating {@link XMemcachedCache} with given name and {@link XMemcachedCacheProperties}.
     *
     * @param name       must not be {@literal null}.
     * @param properties can be {@literal null}.
     */
    private XMemcachedCache createCache(String name, XMemcachedCacheProperties properties) {
        return new XMemcachedCache(name, properties.getExpire(), memcachedClient, properties.isAllowNullValues());
    }

    private void doWith(final Class<?> clazz) {
        ReflectionUtils.doWithMethods(clazz, method -> {
                    ReflectionUtils.makeAccessible(method);
                    Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
                    Caching caching = AnnotationUtils.findAnnotation(method, Caching.class);
                    CacheConfig cacheConfig = AnnotationUtils.findAnnotation(clazz, CacheConfig.class);
                    // 自定义注解
                    Expired expired = AnnotationUtils.findAnnotation(method, Expired.class);

                    List<String> cacheNames = CacheUtils.getCacheNames(cacheable, caching, cacheConfig);
                    add(cacheNames, expired);
                }, method ->
                        null != AnnotationUtils.findAnnotation(method, Cacheable.class)
                                || null != AnnotationUtils.findAnnotation(method, Caching.class)
                                || null != AnnotationUtils.findAnnotation(method, CacheConfig.class)
                                || null != AnnotationUtils.findAnnotation(method, Expired.class)
        );
    }

    private void add(List<String> cacheNames, Expired expired) {
        for (String cacheName : cacheNames) {
            if (cacheName == null || "".equals(cacheName.trim())) {
                continue;
            }
            if (initialCacheConfiguration.get(cacheName) != null) {
                return;
            }
            int expire;
            // 没有配置 @Expired 注解的情况，使用全局配置
            if (expired == null) {
                expire = this.grobalExpire;
                if (log.isWarnEnabled()) {
                    log.warn("Use global configuration. cacheName: {}, expire: {}s",
                            cacheName,
                            expire
                    );
                }
            } else {
                // 如果过期时间配置等于0，则不过期
                expire = expired.value();
                if (expire >= 0) {
                    if (log.isInfoEnabled()) {
                        log.info("Custom configuration. cacheName: {}, expire: {}s",
                                cacheName,
                                expire
                        );
                    }
                } else {
                    expire = this.grobalExpire;
                    if (log.isWarnEnabled()) {
                        log.warn("Custom configuration. cacheName: {}, expire: {}s",
                                cacheName,
                                expire
                        );
                    }
                }
            }
            // 缓存配置
            XMemcachedCacheProperties properties = new XMemcachedCacheProperties(
                    expire,
                    this.allowNullValues
            );
            initialCacheConfiguration.put(cacheName, properties);
        }
    }
}
