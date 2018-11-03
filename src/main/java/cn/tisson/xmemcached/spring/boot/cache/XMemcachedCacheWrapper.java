package cn.tisson.xmemcached.spring.boot.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/**
 * @author Created by YL on 2018/11/2
 */
@Slf4j
public class XMemcachedCacheWrapper implements Cache {
    private final Cache cache;

    public XMemcachedCacheWrapper(Cache cache) {
        this.cache = cache;
    }

    @Override
    public String getName() {
        log.info("name: {}", cache.getName());
        try {
            return cache.getName();
        } catch (Exception e) {
            log.error("getName ---> errmsg: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Object getNativeCache() {
        // log.info("nativeCache: {}", cache.getNativeCache());
        try {
            return cache.getNativeCache();
        } catch (Exception e) {
            log.error("getNativeCache ---> errmsg: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public ValueWrapper get(Object o) {
        log.info("get ---> o: {}", o);
        try {
            return cache.get(o);
        } catch (Exception e) {
            log.error("get ---> o: {}, errmsg: {}", o, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public <T> T get(Object o, Class<T> aClass) {
        log.info("get ---> o: {}, clazz: {}", o, aClass);
        try {
            return cache.get(o, aClass);
        } catch (Exception e) {
            log.error("get ---> o: {}, clazz: {}, errmsg: {}", o, aClass, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public <T> T get(Object o, Callable<T> callable) {
        log.info("get ---> o: {}", o);
        try {
            return cache.get(o, callable);
        } catch (Exception e) {
            log.error("get ---> o: {}, errmsg: {}", o, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void put(Object o, Object o1) {
        log.info("put ---> o: {}, o1: {}", o, o1);
        try {
            cache.put(o, o1);
        } catch (Exception e) {
            log.error("put ---> o: {}, o1: {}, errmsg: {}", o, o1, e.getMessage(), e);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o1) {
        log.info("putIfAbsent ---> o: {}, o1: {}", o, o1);
        try {
            return cache.putIfAbsent(o, o1);
        } catch (Exception e) {
            log.error("putIfAbsent ---> o: {}, o1: {}, errmsg: {}", o, o1, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void evict(Object o) {
        log.info("evict ---> o: {}", o);
        try {
            cache.evict(o);
        } catch (Exception e) {
            log.error("evict ---> o: {}, errmsg: {}", o, e.getMessage(), e);
        }
    }

    @Override
    public void clear() {
        log.info("clear");
        try {
            cache.clear();
        } catch (Exception e) {
            log.error("clear ---> errmsg: {}", e.getMessage(), e);
        }
    }
}
