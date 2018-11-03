package cn.tisson.xmemcached.spring.boot.config.type;

/**
 * 分布策略
 *
 * @author Created by YL on 2018/11/3
 */
public enum SessionLocator {
    /**
     * 一致性哈希：{@link net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator}
     */
    Ketam,

    /**
     * {@link net.rubyeye.xmemcached.impl.ArrayMemcachedSessionLocator}
     */
    Array,
    /**
     * {@link net.rubyeye.xmemcached.impl.ElectionMemcachedSessionLocator}
     */
    Election,

    /**
     * {@link net.rubyeye.xmemcached.impl.LibmemcachedMemcachedSessionLocator}
     */
    Libmemcached,
    /**
     * {@link net.rubyeye.xmemcached.impl.PHPMemcacheSessionLocator}
     */
    PHP,
    /**
     * {@link net.rubyeye.xmemcached.impl.RandomMemcachedSessionLocaltor}
     */
    Random,
    /**
     * {@link net.rubyeye.xmemcached.impl.RoundRobinMemcachedSessionLocator}
     */
    RoundRobin
}
