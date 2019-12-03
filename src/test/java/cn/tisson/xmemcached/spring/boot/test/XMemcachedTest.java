package cn.tisson.xmemcached.spring.boot.test;

import cn.tisson.xmemcached.spring.boot.service.TestService;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author YL
 */
public class XMemcachedTest extends TestCase {
    @Resource
    private TestService testService;

    @Test
    public void get() {
        Map<String, Object> map = testService.get("yangli");
        System.out.println(map);
    }

    @Test
    public void getSupportSpEl() {
        Map<String, Object> map = testService.get("yangli", 300);
        System.out.println(map);
        map = testService.get("yangli", 300);
        System.out.println(map);
    }
}
