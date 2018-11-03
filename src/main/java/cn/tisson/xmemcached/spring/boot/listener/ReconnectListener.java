package cn.tisson.xmemcached.spring.boot.listener;

import lombok.extern.slf4j.Slf4j;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientStateListener;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 重连监听器
 *
 * @author Created by YL on 2018/11/2
 */
@Slf4j
public class ReconnectListener implements MemcachedClientStateListener {
    /**
     * 断开连接的服务器
     */
    private final Map<InetSocketAddress, Long> disconnectedServers = new HashMap<>();

    /**
     * 最大等待重连时间，单位：ms
     */
    private final long maxAwayTime;

    public ReconnectListener(long maxAwayTime) {
        this.maxAwayTime = maxAwayTime;
    }

    public void onConnected(final MemcachedClient memcachedClient, final InetSocketAddress inetSocketAddress) {
        Long disconnectedTime = this.disconnectedServers.get(inetSocketAddress);
        if (disconnectedTime != null && System.currentTimeMillis() - disconnectedTime >= this.maxAwayTime) {
            log.info("Memcached server {} is back and will be flushed", inetSocketAddress);
            (new Thread(new Runnable() {
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException var3) {
                        // ignore
                    }

                    try {
                        log.info("Flushing on memcached server {}", inetSocketAddress);
                        memcachedClient.flushAll(inetSocketAddress);
                        log.info("Memcached server {} flushed successfuly", inetSocketAddress);
                    } catch (Exception var2) {
                        log.error("An error occured while flushing {}",
                                inetSocketAddress.toString(), var2);
                    }
                }
            })).start();
        }

        this.disconnectedServers.remove(inetSocketAddress);
    }

    public void onDisconnected(MemcachedClient memcachedClient, InetSocketAddress inetSocketAddress) {
        this.disconnectedServers.put(inetSocketAddress, System.currentTimeMillis());
    }

    public void onException(MemcachedClient memcachedClient, Throwable throwable) {
    }

    public void onShutDown(MemcachedClient memcachedClient) {
    }

    public void onStarted(MemcachedClient memcachedClient) {
    }
}
