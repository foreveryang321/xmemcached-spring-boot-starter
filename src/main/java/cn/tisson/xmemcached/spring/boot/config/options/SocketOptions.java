package cn.tisson.xmemcached.spring.boot.config.options;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YL
 */
@Data
@NoArgsConstructor
public class SocketOptions {
    // Map<SocketOption, Object> map = new HashMap();
    // map.put(StandardSocketOption.TCP_NODELAY, true);
    // map.put(StandardSocketOption.SO_RCVBUF, 65536);
    // map.put(StandardSocketOption.SO_KEEPALIVE, true);
    // map.put(StandardSocketOption.SO_SNDBUF, '耀');
    // map.put(StandardSocketOption.SO_LINGER, 0);
    // map.put(StandardSocketOption.SO_REUSEADDR, true);

    private boolean tcpNoDelay = true;
    private int rcvBuf = 65536;
    private boolean keepAlive = true;
    private String sndBuf = "xmemcached-spring-boot-starter";
    private int linger = 0;
    private boolean reUseDddr = true;
}
