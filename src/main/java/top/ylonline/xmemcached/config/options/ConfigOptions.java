package top.ylonline.xmemcached.config.options;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YL
 */
@Data
@NoArgsConstructor
public class ConfigOptions {
    /**
     * Read buffer size per connection
     */
    private int sessionReadBufferSize = 32768;
    /**
     * Socket SO_TIMEOUT option
     */
    private int soTimeout = 0;
    /**
     * Thread count for processing WRITABLE event
     */
    private int writeThreadCount = 0;
    /**
     * Whether to enable statistics
     */
    private boolean statisticsServer = false;
    /**
     * Whether to handle read write concurrently,default is true
     */
    private boolean handleReadWriteConcurrently = true;
    /**
     * Thread coount for processing message dispatching
     */
    private int dispatchMessageThreadCount = 0;
    /**
     * THread count for processing READABLE event
     */
    private int readThreadCount = 1;

    // /**
    //  * default: system thread count
    //  */
    // private int selectorPoolSize = System.getProperty("xmemcached.selector.pool.size") == null ?
    //         SystemUtils.getSystemThreadCount() : Integer.parseInt(System.getProperty("xmemcached.selector.pool
    // .size"));

    /**
     * check session idle interval
     */
    private long checkSessionTimeoutInterval = 1000L;
    private long sessionIdleTimeout = 5000L;
    protected long statisticsInterval = 300000L;
}
