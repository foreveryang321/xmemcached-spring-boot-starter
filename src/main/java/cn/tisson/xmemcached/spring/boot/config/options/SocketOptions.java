package cn.tisson.xmemcached.spring.boot.config.options;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Created by YL on 2018/11/2
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
    private int soRcvBuf = 65536;
    private boolean soKeepAlive = true;
    private String soSndBuf = "xmemcached-spring-boot-starter";
    private int soLinger = 0;
    private boolean soReUseDddr = true;
}
