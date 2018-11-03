package cn.tisson.xmemcached.spring.boot;

import cn.tisson.xmemcached.spring.boot.annotation.EnableXMemcachedConfiguration;
import cn.tisson.xmemcached.spring.boot.cache.XMemcachedCacheManager;
import cn.tisson.xmemcached.spring.boot.config.XMemcachedProperties;
import cn.tisson.xmemcached.spring.boot.factory.XMemcacheClientFactory;
import lombok.extern.slf4j.Slf4j;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Created by YL on 2018/11/1
 */
@Configuration
@ConditionalOnBean(annotation = {
        EnableXMemcachedConfiguration.class
})
@ConditionalOnClass(value = {
        XMemcachedClientBuilder.class
})
@EnableConfigurationProperties({
        XMemcachedProperties.class
})
@EnableCaching(proxyTargetClass = true)
@Slf4j
public class XMemcachedAutoConfiguration extends CachingConfigurerSupport {
    private XMemcachedProperties xMemcachedProperties;

    @Autowired
    public XMemcachedAutoConfiguration(XMemcachedProperties xMemcachedProperties) {
        this.xMemcachedProperties = xMemcachedProperties;
    }

    @Bean
    public MemcachedClient memcachedClient() throws IOException {
        return new XMemcacheClientFactory().create(xMemcachedProperties);
    }

    // @Override
    // public KeyGenerator keyGenerator() {
    //     return super.keyGenerator();
    // }

    @Bean
    @Override
    public CacheManager cacheManager() {
        try {
            return new XMemcachedCacheManager(memcachedClient(), xMemcachedProperties.isAllowNullValues());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
