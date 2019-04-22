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
    /**
     * @see MemcachedClient#DEFAULT_TCP_NO_DELAY
     */
    private boolean tcpNoDelay = true;
    /**
     * @see MemcachedClient#DEFAULT_TCP_RECV_BUFF_SIZE
     */
    private int rcvBuf = 65536;
    /**
     * @see MemcachedClient#DEFAULT_TCP_KEEPLIVE
     */
    private boolean keepAlive = true;
    /**
     * @see MemcachedClient#DEFAULT_TCP_SEND_BUFF_SIZE
     */
    private int sndBuf = 32768;
    private int linger = 0;
    private boolean reUseAddr = true;
}
