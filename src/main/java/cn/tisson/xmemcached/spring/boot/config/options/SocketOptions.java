package cn.tisson.xmemcached.spring.boot.config.options;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.rubyeye.xmemcached.MemcachedClient;

/**
 * @author YL
 */
@Data
@NoArgsConstructor
public class SocketOptions {
    private boolean tcpNoDelay = MemcachedClient.DEFAULT_TCP_NO_DELAY;
    private int rcvBuf = MemcachedClient.DEFAULT_TCP_RECV_BUFF_SIZE;
    private boolean keepAlive = MemcachedClient.DEFAULT_TCP_KEEPLIVE;
    private int sndBuf = MemcachedClient.DEFAULT_TCP_SEND_BUFF_SIZE;
    private int linger = 0;
    private boolean reUseAddr = true;
}
