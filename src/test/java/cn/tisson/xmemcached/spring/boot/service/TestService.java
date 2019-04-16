package cn.tisson.xmemcached.spring.boot.service;

import cn.tisson.xmemcached.spring.boot.annotation.CacheExpire;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YL
 */
@Service
public class TestService {

    @Cacheable(cacheNames = "add", key = "#p0", unless = "#result eq null")
    @CacheExpire(90)
    public Map<String, Object> add(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("id", System.currentTimeMillis());
        return map;
    }

    @Cacheable(cacheNames = "put", key = "#p0", unless = "#result eq null")
    @CacheExpire
    public Map<String, Object> put(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("id", System.currentTimeMillis());
        return map;
    }
}
