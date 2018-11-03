package cn.tisson.xmemcached.spring.boot.config;

import cn.tisson.xmemcached.spring.boot.config.options.ConfigOptions;
import cn.tisson.xmemcached.spring.boot.config.options.SocketOptions;
import cn.tisson.xmemcached.spring.boot.config.type.CommandFactory;
import cn.tisson.xmemcached.spring.boot.config.type.SessionLocator;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author Created by YL on 2018/11/2
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.xmemcached")
public class XMemcachedProperties {
    private String address;
    private int[] weights;

    private long connectTimeout = 60000;

    private int connectionPoolSize = 1;

    private long opTimeout = 5000;
    private boolean optimizeGet = false;

    private boolean failureMode = false;

    // private long healConnectionInterval = 11L;
    // private boolean enableHealSession = false;
    //
    // private int transcoderMaxSize = 20971520;
    //
    private boolean allowNullValues = false;

    /**
     * reconnect time, 单位：ms。如果小于等于 0，则不配置重连监听，大于0时才配置
     */
    private long maxAwayTime = 60000;

    /**
     * 分布策略
     */
    private SessionLocator sessionLocator = SessionLocator.Array;
    /**
     * 协议工场
     */
    private CommandFactory commandFactory = CommandFactory.Text;

    @NestedConfigurationProperty
    private ConfigOptions configOptions;

    @NestedConfigurationProperty
    private SocketOptions socketOptions;
}
