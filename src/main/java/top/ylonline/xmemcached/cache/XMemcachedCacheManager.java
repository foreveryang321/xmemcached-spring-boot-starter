package top.ylonline.xmemcached.cache;

import lombok.extern.slf4j.Slf4j;
import net.rubyeye.xmemcached.MemcachedClient;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.lang.Nullable;
import top.ylonline.xmemcached.cache.util.CacheUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author YL
 */
@Slf4j
public class XMemcachedCacheManager extends AbstractTransactionSupportingCacheManager {
    private final MemcachedClient memcachedClient;
    private final int globalExpire;
    private final boolean allowNullValues;
    private final Map<String, XMemcachedCacheProperties> initialCacheConfiguration;

    public XMemcachedCacheManager(MemcachedClient memcachedClient, int globalExpire, boolean allowNullValues, String... initialCacheNames) {
        this.memcachedClient = memcachedClient;
        this.globalExpire = globalExpire;
        this.allowNullValues = allowNullValues;
        this.initialCacheConfiguration = new LinkedHashMap<>();
        for (String cacheName : initialCacheNames) {
            XMemcachedCacheProperties properties = new XMemcachedCacheProperties(
                    this.globalExpire,
                    this.allowNullValues
            );
            this.initialCacheConfiguration.put(cacheName, properties);
        }
    }

    @Override
    protected Collection<XMemcachedCache> loadCaches() {
        List<XMemcachedCache> caches = new LinkedList<>();
        for (Map.Entry<String, XMemcachedCacheProperties> entry : initialCacheConfiguration.entrySet()) {
            XMemcachedCacheProperties properties = entry.getValue();
            caches.add(createCache(entry.getKey(), properties.getExpire(), properties.isAllowNullValues()));
        }
        return caches;
    }

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    @Override
    public Cache getCache(String name) {
        if (log.isDebugEnabled()) {
            log.debug("getCache ---> name: {}", name);
        }
        // Quick check for existing cache...
        Cache cache = this.cacheMap.get(name);
        if (cache != null) {
            return cache;
        }
        String[] array = CacheUtils.splitCacheNameForTtl(name);
        String cacheName = array[0];
        cache = super.getCache(cacheName);
        if (cache != null && array.length > 1) {
            int ttl = Integer.parseInt(array[1]);
            if (log.isDebugEnabled()) {
                log.debug("getCache ---> cacheName: {}, ttl: {}", cacheName, ttl);
            }
            cache = this.createCache(cacheName, ttl, this.allowNullValues);
            cacheMap.put(name, cache);
        }
        return cache;
    }

    @Override
    @Nullable
    protected Cache getMissingCache(String name) {
        return createCache(name, this.globalExpire, this.allowNullValues);
    }

    /**
     * Configuration hook for creating {@link XMemcachedCache} with given name and {@link XMemcachedCacheProperties}.
     *
     * @param name            must not be {@literal null}.
     * @param expire          can be {@literal null}.
     * @param allowNullValues can be {@literal null}.
     */
    private XMemcachedCache createCache(String name, int expire, boolean allowNullValues) {
        return new XMemcachedCache(name, expire, memcachedClient, allowNullValues);
    }
}
