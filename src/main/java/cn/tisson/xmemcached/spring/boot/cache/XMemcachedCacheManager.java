package cn.tisson.xmemcached.spring.boot.cache;

import cn.tisson.xmemcached.spring.boot.annotation.CacheExpire;
import cn.tisson.xmemcached.spring.boot.util.CacheUtils;
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
    private boolean allowNullValues;
    private Map<String, XMemcachedCacheConfiguration> initialCacheConfiguration = new LinkedHashMap<>();

    private ApplicationContext applicationContext;

    public XMemcachedCacheManager(MemcachedClient memcachedClient, boolean allowNullValues) {
        this.memcachedClient = memcachedClient;
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
            final Class clazz = applicationContext.getType(beanName);
            doWith(clazz);
        }
        super.afterPropertiesSet();
    }

    @Override
    protected Collection<XMemcachedCache> loadCaches() {
        List<XMemcachedCache> caches = new LinkedList<>();

        for (Map.Entry<String, XMemcachedCacheConfiguration> entry : initialCacheConfiguration.entrySet()) {
            caches.add(createCache(entry.getKey(), entry.getValue()));
        }
        return caches;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = super.getCache(name);
        return new XMemcachedCacheWrapper(cache);
    }

    /**
     * Configuration hook for creating {@link XMemcachedCache} with given name and {@link XMemcachedCacheConfiguration}.
     *
     * @param name          must not be {@literal null}.
     * @param configuration can be {@literal null}.
     */
    private XMemcachedCache createCache(String name, XMemcachedCacheConfiguration configuration) {
        return new XMemcachedCache(name, configuration.getExpire(), memcachedClient, configuration.isAllowNullValues());
    }

    private void doWith(final Class clazz) {
        ReflectionUtils.doWithMethods(clazz, method -> {
            ReflectionUtils.makeAccessible(method);
            Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
            Caching caching = AnnotationUtils.findAnnotation(method, Caching.class);
            CacheConfig cacheConfig = AnnotationUtils.findAnnotation(clazz, CacheConfig.class);
            // 自定义注解
            CacheExpire cacheExpire = AnnotationUtils.findAnnotation(method, CacheExpire.class);

            List<String> cacheNames = CacheUtils.getCacheNames(cacheable, caching, cacheConfig);
            add(cacheNames, cacheExpire);
        }, method -> null != AnnotationUtils.findAnnotation(method, CacheExpire.class));
    }

    private void add(List<String> cacheNames, CacheExpire cacheExpire) {
        for (String cacheName : cacheNames) {
            if (cacheName == null || "".equals(cacheName.trim())) {
                continue;
            }
            int expire = cacheExpire.value();
            if (log.isInfoEnabled()) {
                log.info("cacheNames: {}, expire: {}s", cacheNames, expire);
            }
            if (expire > 0) {
                // 缓存配置
                XMemcachedCacheConfiguration config = new XMemcachedCacheConfiguration(expire,
                        this.allowNullValues);
                initialCacheConfiguration.put(cacheName, config);
            } else {
                log.warn("{} use default expiration.", cacheName);
            }
        }
    }
}
