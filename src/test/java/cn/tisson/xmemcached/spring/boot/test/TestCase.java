package cn.tisson.xmemcached.spring.boot.test;

import cn.tisson.xmemcached.spring.boot.App;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Created by YL on 2018/11/1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@ActiveProfiles("local")
@Slf4j
public class TestCase {
}
