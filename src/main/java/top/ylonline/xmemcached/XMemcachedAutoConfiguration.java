package top.ylonline.xmemcached;

import lombok.RequiredArgsConstructor;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import top.ylonline.xmemcached.cache.XMemcachedCacheManager;
import top.ylonline.xmemcached.cache.interceptor.TCacheErrorHandler;
import top.ylonline.xmemcached.cache.interceptor.TCacheResolver;
import top.ylonline.xmemcached.cache.interceptor.TKeyGenerator;
import top.ylonline.xmemcached.config.XMemcachedProperties;
import top.ylonline.xmemcached.factory.XMemcacheClientFactory;

import java.io.IOException;
import java.util.List;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for XMemcached support.
 *
 * @author YL
 */
@Configuration
@ConditionalOnClass({XMemcachedClient.class, XMemcachedClientBuilder.class})
@EnableConfigurationProperties({CacheProperties.class, XMemcachedProperties.class})
@RequiredArgsConstructor
public class XMemcachedAutoConfiguration extends CachingConfigurerSupport {
    private final CacheProperties cacheProperties;
    private final XMemcachedProperties xMemcachedProperties;

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

    /**
     * 如果 @Cacheable、@CachePut、@CacheEvict 等注解没有配置 key，则使用这个自定义 key 生成器
     * <pre>
     *     但自定义了缓存的 key 时，难以保证 key 的唯一性，此时最好指定方法名，比如：@Cacheable(value="", key="{#root.methodName, #id}")
     * </pre>
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new TKeyGenerator();
    }

    @Bean
    @Override
    public CacheResolver cacheResolver() {
        return new TCacheResolver(cacheManager());
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new TCacheErrorHandler();
    }

    /**
     * 配置 XMemcachedCacheManager
     * <pre>
     *     这里一定要加上&#64;{@link Bean}注解
     * </pre>
     */
    @Bean
    @Override
    public CacheManager cacheManager() {
        List<String> cacheNames = cacheProperties.getCacheNames();
        String[] initialCacheNames = new String[]{};
        if (!CollectionUtils.isEmpty(cacheNames)) {
            initialCacheNames = cacheNames.toArray(new String[0]);
        }
        return new XMemcachedCacheManager(
                memcachedClient(),
                xMemcachedProperties.getExpire(),
                xMemcachedProperties.isAllowNullValues(),
                initialCacheNames
        );
    }
}
