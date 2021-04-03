package top.ylonline.xmemcached.spring.boot.test;

import top.ylonline.xmemcached.spring.boot.App;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author YL
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@ActiveProfiles("local")
@EnableCaching(proxyTargetClass = true)
@Slf4j
public class TestCase {
}
