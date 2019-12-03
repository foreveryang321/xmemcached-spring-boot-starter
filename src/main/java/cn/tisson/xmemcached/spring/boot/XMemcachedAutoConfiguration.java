package cn.tisson.xmemcached.spring.boot;

import cn.tisson.xmemcached.spring.boot.anno.EnableXMemcachedConfiguration;
import cn.tisson.xmemcached.spring.boot.cache.TKeyGenerator;
import cn.tisson.xmemcached.spring.boot.cache.TSimpleCacheResolver;
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
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
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
@EnableConfigurationProperties({XMemcachedProperties.class})
public class XMemcachedAutoConfiguration extends CachingConfigurerSupport {
    private final XMemcachedProperties xMemcachedProperties;

    public XMemcachedAutoConfiguration(XMemcachedProperties xMemcachedProperties) {
        this.xMemcachedProperties = xMemcachedProperties;
    }

    @Bean
    @ConditionalOnMissingBean(name = "memcachedClient")
    public MemcachedClient memcachedClient() {
        try {
            return new XMemcacheClientFactory().create(xMemcachedProperties);
        } catch (IOException e) {
            // ignore
        }
        return null;
    }

    @Override
    public KeyGenerator keyGenerator() {
        return new TKeyGenerator();
    }

    @Override
    public CacheResolver cacheResolver() {
        return new TSimpleCacheResolver(cacheManager());
    }

    @Override
    public CacheManager cacheManager() {
        return new XMemcachedCacheManager(
                memcachedClient(),
                xMemcachedProperties.getExpire(),
                xMemcachedProperties.isAllowNullValues()
        );
    }
}
