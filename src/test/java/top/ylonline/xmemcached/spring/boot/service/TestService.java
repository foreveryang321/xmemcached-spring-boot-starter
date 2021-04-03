package top.ylonline.xmemcached.spring.boot.service;

import top.ylonline.xmemcached.spring.boot.anno.Expired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YL
 */
@Service
@Slf4j
public class TestService {

    @Cacheable(cacheNames = "get", key = "#p0", unless = "#result eq null")
    @Expired(90)
    public Map<String, Object> get(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", System.currentTimeMillis());
        map.put("name", name);
        log.info("map: {}", map);
        return map;
    }

    /**
     * 使用 SpEl 表达式配置过期时间
     */
    @Cacheable(cacheNames = "get", key = "#p0", unless = "#result eq null")
    @Expired(value = 90, el = "#expire")
    // @Expired(expire = 90, el = "#p1")
    // @Expired(expire = 90, el = "#a1")
    public Map<String, Object> get(String name, int expire) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", System.currentTimeMillis());
        map.put("name", name);
        map.put("expire", expire);
        log.info("map: {}", map);
        return map;
    }
}
