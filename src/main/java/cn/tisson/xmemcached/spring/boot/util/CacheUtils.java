package cn.tisson.xmemcached.spring.boot.util;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 缓存工具
 *
 * @author Created by YL on 2018/11/1
 */
public class CacheUtils {
    /**
     * 获取 cacheNames
     * <pre>
     *     处理优先级 cacheable > caching > cacheConfig
     * </pre>
     *
     * @param cacheable   缓存注解 Cacheable
     * @param caching     缓存注解 Caching
     * @param cacheConfig 缓存注解 CacheConfig
     */
    public static List<String> getCacheNames(Cacheable cacheable, Caching caching, CacheConfig cacheConfig) {
        List<String> list = new ArrayList<>();
        // Cacheable
        if (cacheable != null) {
            String[] cacheNames = cacheable.cacheNames();
            if (cacheNames.length > 0) {
                list.addAll(Arrays.asList(cacheNames));
            }
        }
        if (list.size() > 0) {
            return list;
        }

        // Caching
        if (caching != null) {
            Cacheable[] cacheables = caching.cacheable();
            for (Cacheable cache : cacheables) {
                String[] cacheNames = cache.cacheNames();
                if (cacheNames.length > 0) {
                    list.addAll(Arrays.asList(cacheNames));
                }
            }
        }
        if (list.size() > 0) {
            return list;
        }

        // CacheConfig
        if (cacheConfig != null) {
            String[] cacheNames = cacheConfig.cacheNames();
            if (cacheNames.length > 0) {
                list.addAll(Arrays.asList(cacheNames));
            }
        }
        return list;
    }
}
