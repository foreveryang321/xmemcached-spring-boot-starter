package top.ylonline.xmemcached.spring.boot.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/**
 * @author YL
 */
@Slf4j
public class XMemcachedCacheWrapper implements Cache {
    private final Cache cache;

    public XMemcachedCacheWrapper(Cache cache) {
        this.cache = cache;
    }

    @Override
    public String getName() {
        if (log.isDebugEnabled()) {
            log.debug("name: {}", cache.getName());
        }
        return cache.getName();
    }

    @Override
    public Object getNativeCache() {
        // log.info("nativeCache: {}", cache.getNativeCache());
        return cache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object o) {
        if (log.isDebugEnabled()) {
            log.debug("get ---> o: {}", o);
        }
        try {
            return cache.get(o);
        } catch (Exception e) {
            log.error("get ---> o: {}, errmsg: {}", o, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public <T> T get(Object o, Class<T> aClass) {
        if (log.isDebugEnabled()) {
            log.debug("get ---> o: {}, clazz: {}", o, aClass);
        }
        try {
            return cache.get(o, aClass);
        } catch (Exception e) {
            log.error("get ---> o: {}, clazz: {}, errmsg: {}", o, aClass, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public <T> T get(Object o, Callable<T> callable) {
        if (log.isDebugEnabled()) {
            log.debug("get ---> o: {}", o);
        }
        try {
            return cache.get(o, callable);
        } catch (Exception e) {
            log.error("get ---> o: {}, errmsg: {}", o, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void put(Object o, Object o1) {
        if (log.isDebugEnabled()) {
            log.debug("put ---> o: {}, o1: {}", o, o1);
        }
        try {
            cache.put(o, o1);
        } catch (Exception e) {
            log.error("put ---> o: {}, o1: {}, errmsg: {}", o, o1, e.getMessage(), e);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o1) {
        if (log.isDebugEnabled()) {
            log.debug("putIfAbsent ---> o: {}, o1: {}", o, o1);
        }
        try {
            return cache.putIfAbsent(o, o1);
        } catch (Exception e) {
            log.error("putIfAbsent ---> o: {}, o1: {}, errmsg: {}", o, o1, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void evict(Object o) {
        if (log.isDebugEnabled()) {
            log.debug("evict ---> o: {}", o);
        }
        try {
            cache.evict(o);
        } catch (Exception e) {
            log.error("evict ---> o: {}, errmsg: {}", o, e.getMessage(), e);
        }
    }

    @Override
    public void clear() {
        if (log.isDebugEnabled()) {
            log.debug("clear");
        }
        try {
            cache.clear();
        } catch (Exception e) {
            log.error("clear ---> errmsg: {}", e.getMessage(), e);
        }
    }
}
