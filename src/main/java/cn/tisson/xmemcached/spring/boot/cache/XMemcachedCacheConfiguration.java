package cn.tisson.xmemcached.spring.boot.cache;

import lombok.Getter;

/**
 * @author Created by YL on 2018/11/2
 */
@Getter
public class XMemcachedCacheConfiguration {
    private int expire;
    private boolean allowNullValues;

    /**
     * Create new {@link XMemcachedCacheConfiguration}.
     *
     * @param expire          must not be {@literal null}.
     * @param allowNullValues must not be {@literal null}.
     */
    public XMemcachedCacheConfiguration(int expire, boolean allowNullValues) {
        this.expire = expire;
        this.allowNullValues = allowNullValues;
    }
}
