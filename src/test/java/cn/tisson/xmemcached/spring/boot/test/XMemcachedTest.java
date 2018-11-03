package cn.tisson.xmemcached.spring.boot.test;

import cn.tisson.xmemcached.spring.boot.service.TestService;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Created by YL on 2018/11/1
 */
public class XMemcachedTest extends TestCase {
    @Resource
    private TestService testService;

    @Test
    public void add() {
        Map<String, Object> map = testService.add("yangli");
        System.out.println(map);
        Map<String, Object> yangli123 = testService.put("yangli123");
        System.out.println(yangli123);
    }
}
