package top.ylonline.xmemcached.cache;

import lombok.Getter;

/**
 * @author YL
 */
@Getter
public class XMemcachedCacheProperties {
    private final int expire;
    private final boolean allowNullValues;

    /**
     * Create new {@link XMemcachedCacheProperties}.
     *
     * @param expire          must not be {@literal null}.
     * @param allowNullValues must not be {@literal null}.
     */
    public XMemcachedCacheProperties(int expire, boolean allowNullValues) {
        this.expire = expire;
        this.allowNullValues = allowNullValues;
    }
}
