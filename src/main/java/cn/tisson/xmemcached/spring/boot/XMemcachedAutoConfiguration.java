package cn.tisson.xmemcached.spring.boot;

import cn.tisson.xmemcached.spring.boot.anno.EnableXMemcachedConfiguration;
import cn.tisson.xmemcached.spring.boot.cache.XMemcachedCacheManager;
import cn.tisson.xmemcached.spring.boot.config.XMemcachedProperties;
import cn.tisson.xmemcached.spring.boot.factory.XMemcacheClientFactory;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for XMemcached support.
 *
 * @author YL
 */
@Configuration
@ConditionalOnBean(annotation = {
        EnableXMemcachedConfiguration.class
})
@ConditionalOnClass({XMemcachedClient.class, XMemcachedClientBuilder.class})
@EnableConfigurationProperties(XMemcachedProperties.class)
public class XMemcachedAutoConfiguration {
    private final XMemcachedProperties xMemcachedProperties;

    public XMemcachedAutoConfiguration(XMemcachedProperties xMemcachedProperties) {
        this.xMemcachedProperties = xMemcachedProperties;
    }

    @Bean
    @ConditionalOnMissingBean(name = "memcachedClient")
    public MemcachedClient memcachedClient() throws IOException {
        return new XMemcacheClientFactory().create(xMemcachedProperties);
    }

    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public XMemcachedCacheManager cacheManager(MemcachedClient memcachedClient) {
        return new XMemcachedCacheManager(
                memcachedClient,
                xMemcachedProperties.getExpire(),
                xMemcachedProperties.isAllowNullValues()
        );
    }
}
