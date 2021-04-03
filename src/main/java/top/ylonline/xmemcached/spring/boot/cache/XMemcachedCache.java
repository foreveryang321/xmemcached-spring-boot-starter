package top.ylonline.xmemcached.spring.boot.cache;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.NullValue;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

/**
 * {@link org.springframework.cache.Cache} implementation using for Memcached as underlying store.
 *
 * @author YL
 */
public class XMemcachedCache extends AbstractValueAdaptingCache {
    private String name;
    private int expire;
    private MemcachedClient memcachedClient;

    /**
     * Create new {@link XMemcachedCache}.
     *
     * @param name            must not be {@literal null}.
     * @param memcachedClient must not be {@literal null}.
     */
    public XMemcachedCache(String name, int expire, MemcachedClient memcachedClient, boolean allowNullValues) {
        super(allowNullValues);
        this.name = name;
        this.expire = expire;
        this.memcachedClient = memcachedClient;
    }

    @Override
    protected Object lookup(Object o) {
        Object object = null;
        try {
            object = memcachedClient.get((String) o);
        } catch (TimeoutException | InterruptedException | MemcachedException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MemcachedClient getNativeCache() {
        return memcachedClient;
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        ValueWrapper result = get(key);

        if (result != null) {
            return (T) result.get();
        }

        T value = valueFromLoader(key, callable);
        put(key, value);
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        Object cacheValue = preProcessCacheValue(value);
        if (!isAllowNullValues() && cacheValue == null) {
            throw new IllegalArgumentException(String.format(
                    "Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == " +
                            "null\")' or configure XMemcachedCache to allow 'null' via XMemcachedCacheConfiguration.",
                    name));
        }
        try {
            memcachedClient.set((String) key, expire, value);
        } catch (TimeoutException | InterruptedException | MemcachedException e) {
            // 如果写入时出现异常，则删除缓存（防止db、缓存数据不一致，不太优雅的解决法案）
            this.evict(key);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Object cacheValue = preProcessCacheValue(value);

        if (!isAllowNullValues() && cacheValue == null) {
            return get(key);
        }
        try {
            boolean isSet = memcachedClient.set((String) key, expire, value);
            if (isSet) {
                return this.toValueWrapper(value);
            }
        } catch (TimeoutException | InterruptedException | MemcachedException e) {
            // 如果写入时出现异常，则删除缓存（防止db、缓存数据不一致，不太优雅的解决法案）
            this.evict(key);
        }
        return null;
    }

    @Override
    public void evict(Object key) {
        try {
            memcachedClient.delete((String) key);
        } catch (TimeoutException | MemcachedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        try {
            memcachedClient.flushAll();
        } catch (TimeoutException | InterruptedException | MemcachedException e) {
            e.printStackTrace();
        }
    }

    private Object preProcessCacheValue(Object value) {
        if (value != null) {
            return value;
        }

        return isAllowNullValues() ? NullValue.INSTANCE : null;
    }

    private static <T> T valueFromLoader(Object key, Callable<T> valueLoader) {

        try {
            return valueLoader.call();
        } catch (Exception e) {
            throw new ValueRetrievalException(key, valueLoader, e);
        }
    }
}
