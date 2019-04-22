package cn.tisson.xmemcached.spring.boot.config;

import cn.tisson.xmemcached.spring.boot.config.options.ConfigOptions;
import cn.tisson.xmemcached.spring.boot.config.options.SocketOptions;
import cn.tisson.xmemcached.spring.boot.config.type.CommandFactory;
import cn.tisson.xmemcached.spring.boot.config.type.SessionLocator;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.impl.Optimizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author YL
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.xmemcached")
public class XMemcachedProperties {
    private String address;
    private int[] weights;

    private long connectTimeout = 60000;

    private int connectionPoolSize = 1;

    /**
     * global expire time
     * <p>
     * default：3600s
     * </p>
     */
    private int expire = 3600;

    private long opTimeout = 5000;
    private boolean optimizeGet = true;
    /**
     * Enable/Disable merge many command's buffers to one big buffer fit socket's send buffer
     * size.Default is true.Recommend true.
     */
    private boolean optimizeMergeBuffer = true;

    private boolean failureMode = false;

    /**
     * global allow null values
     * <p>
     * default：false
     * </p>
     */
    private boolean allowNullValues = false;

    /**
     * reconnect time, 单位：ms。如果小于等于 0，则不配置重连监听，大于0时才配置
     */
    private long maxAwayTime = 60000;

    // client
    /**
     * Set the merge factor,this factor determins how many 'get' commands would be merge to one
     * multi-get command.default is 150
     *
     * @see Optimizer#DEFAULT_MERGE_FACTOR
     */
    private int mergeFactor = 50;

    /**
     * If the memcached dump or network error cause connection closed,xmemcached would try to heal the
     * connection.The interval between reconnections is 2 seconds by default. You can change that
     * value by this method.
     */
    private long healConnectionInterval = 2;

    /**
     * If the memcached dump or network error cause connection closed,xmemcached would try to heal the
     * connection.You can disable this behaviour by using this method:<br/>
     * <code> client.setEnableHealSession(false); </code><br/>
     * The default value is true.
     */
    private boolean enableHealSession = true;

    /**
     * Store all primitive type as string,defualt is false.
     */
    private boolean primitiveAsString = false;

    /**
     * Whether to enable heart beat
     */
    private boolean enableHeartBeat = true;

    /**
     * Enables/disables sanitizing keys by URLEncoding.
     */
    private boolean sanitizeKey = false;

    // /**
    //  * Set max queued noreply operations number
    //  *
    //  * @see MemcachedClient#DEFAULT_MAX_QUEUED_NOPS
    //  */
    // private int maxQueuedNoReplyOperations;

    /**
     * SessionLocator
     */
    private SessionLocator sessionLocator = SessionLocator.Array;
    /**
     * CommandFactory
     */
    private CommandFactory commandFactory = CommandFactory.Text;

    @NestedConfigurationProperty
    private ConfigOptions configOptions = new ConfigOptions();

    @NestedConfigurationProperty
    private SocketOptions socketOptions = new SocketOptions();
}
