package cn.tisson.xmemcached.spring.boot.factory;

import cn.tisson.xmemcached.spring.boot.config.XMemcachedProperties;
import cn.tisson.xmemcached.spring.boot.config.options.ConfigOptions;
import cn.tisson.xmemcached.spring.boot.config.options.SocketOptions;
import cn.tisson.xmemcached.spring.boot.config.type.CommandFactory;
import cn.tisson.xmemcached.spring.boot.config.type.SessionLocator;
import cn.tisson.xmemcached.spring.boot.listener.ReconnectListener;
import com.google.code.yanf4j.config.Configuration;
import com.google.code.yanf4j.core.impl.StandardSocketOption;
import com.google.code.yanf4j.util.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.command.KestrelCommandFactory;
import net.rubyeye.xmemcached.command.TextCommandFactory;
import net.rubyeye.xmemcached.impl.ArrayMemcachedSessionLocator;
import net.rubyeye.xmemcached.impl.ElectionMemcachedSessionLocator;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.impl.LibmemcachedMemcachedSessionLocator;
import net.rubyeye.xmemcached.impl.PHPMemcacheSessionLocator;
import net.rubyeye.xmemcached.impl.RandomMemcachedSessionLocaltor;
import net.rubyeye.xmemcached.impl.RoundRobinMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;
import net.rubyeye.xmemcached.utils.Protocol;

import java.io.IOException;

/**
 * @author YL
 */
@Slf4j
public class XMemcacheClientFactory {
    public XMemcacheClientFactory() {}

    public MemcachedClient create(XMemcachedProperties conf) throws IOException {
        XMemcachedClientBuilder builder = null;
        int[] weights = conf.getWeights();
        if (weights != null && weights.length > 0) {
            builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(conf.getAddress()), weights);
        }

        if (builder == null) {
            builder = new XMemcachedClientBuilder(conf.getAddress());
        }

        builder.setConnectTimeout(conf.getConnectTimeout());
        builder.setConnectionPoolSize(conf.getConnectionPoolSize());

        builder.setOpTimeout(conf.getOpTimeout());

        builder.setEnableHealSession(conf.isEnableHealSession());

        builder.setFailureMode(conf.isFailureMode());

        builder.setSanitizeKeys(conf.isSanitizeKey());

        // builder.setMaxQueuedNoReplyOperations(conf.getMaxQueuedNoReplyOperations());

        SessionLocator sessionLocator = conf.getSessionLocator();
        CommandFactory commandFactory = conf.getCommandFactory();
        this.configureCommandFactoryAndSessionLocator(builder, commandFactory, sessionLocator);

        SocketOptions socketOptions = conf.getSocketOptions();
        this.setSocketOptions(builder, socketOptions);

        ConfigOptions configOptions = conf.getConfigOptions();
        this.setConfiguration(builder, configOptions);

        // 重连监听
        if (conf.getMaxAwayTime() > 0) {
            builder.addStateListener(new ReconnectListener(conf.getMaxAwayTime()));
        }
        this.setProviderBuilderSpecificSettings(builder, conf);

        MemcachedClient client = builder.build();
        this.configureClient(builder, client, conf);
        return client;
    }

    /**
     * 协议工场、分布策略配置
     *
     * @param builder        {@link  XMemcachedClientBuilder}
     * @param command        协议工场
     * @param sessionLocator 分布策略
     */
    private void configureCommandFactoryAndSessionLocator(XMemcachedClientBuilder builder, CommandFactory command,
                                                          SessionLocator sessionLocator) {
        if (CommandFactory.Binary.equals(command)) {
            builder.setCommandFactory(new BinaryCommandFactory());
        } else if (CommandFactory.Text.equals(command)) {
            builder.setCommandFactory(new TextCommandFactory());
        } else if (CommandFactory.Kestrel.equals(command)) {
            builder.setCommandFactory(new KestrelCommandFactory());
        }

        if (SessionLocator.Ketam.equals(sessionLocator)) {
            builder.setSessionLocator(new KetamaMemcachedSessionLocator());
        } else if (SessionLocator.Array.equals(sessionLocator)) {
            builder.setSessionLocator(new ArrayMemcachedSessionLocator());
        } else if (SessionLocator.Election.equals(sessionLocator)) {
            builder.setSessionLocator(new ElectionMemcachedSessionLocator());
        } else if (SessionLocator.Libmemcached.equals(sessionLocator)) {
            builder.setSessionLocator(new LibmemcachedMemcachedSessionLocator());
        } else if (SessionLocator.PHP.equals(sessionLocator)) {
            builder.setSessionLocator(new PHPMemcacheSessionLocator());
        } else if (SessionLocator.Random.equals(sessionLocator)) {
            builder.setSessionLocator(new RandomMemcachedSessionLocaltor());
        } else if (SessionLocator.RoundRobin.equals(sessionLocator)) {
            builder.setSessionLocator(new RoundRobinMemcachedSessionLocator());
        }
        // kestrel protocol use random session locator.
        if (builder.getCommandFactory().getProtocol() == Protocol.Kestrel) {
            if (!(builder.getSessionLocator() instanceof RandomMemcachedSessionLocaltor)) {
                log.warn(
                        "Switch `net.rubyeye.xmemcached.impl.RandomMemcachedSessionLocaltor` as session " +
                                "locator for kestrel protocol.");
                builder.setSessionLocator(new RandomMemcachedSessionLocaltor());
            }
        }
    }

    /**
     * socket options
     */
    private void setSocketOptions(XMemcachedClientBuilder builder, SocketOptions socket) {
        builder.setSocketOption(StandardSocketOption.TCP_NODELAY, socket.isTcpNoDelay());
        builder.setSocketOption(StandardSocketOption.SO_RCVBUF, socket.getRcvBuf());
        builder.setSocketOption(StandardSocketOption.SO_KEEPALIVE, socket.isKeepAlive());
        builder.setSocketOption(StandardSocketOption.SO_SNDBUF, socket.getSndBuf());
        builder.setSocketOption(StandardSocketOption.SO_LINGER, socket.getLinger());
        builder.setSocketOption(StandardSocketOption.SO_REUSEADDR, socket.isReUseDddr());
    }

    private void setConfiguration(XMemcachedClientBuilder builder, ConfigOptions options) {
        Configuration config = new Configuration();
        config.setSessionReadBufferSize(options.getSessionReadBufferSize());
        config.setSoTimeout(options.getSoTimeout());
        config.setWriteThreadCount(options.getWriteThreadCount());
        config.setStatisticsServer(options.isStatisticsServer());
        config.setHandleReadWriteConcurrently(options.isHandleReadWriteConcurrently());
        config.setDispatchMessageThreadCount(options.getDispatchMessageThreadCount());
        config.setReadThreadCount(options.getReadThreadCount());
        // a.setSelectorPoolSize(c.getSelectorPoolSize());
        config.setSelectorPoolSize(System.getProperty(Configuration.XMEMCACHED_SELECTOR_POOL_SIZE) == null ?
                SystemUtils.getSystemThreadCount()
                : Integer.parseInt(System.getProperty(Configuration.XMEMCACHED_SELECTOR_POOL_SIZE)));
        config.setCheckSessionTimeoutInterval(options.getCheckSessionTimeoutInterval());
        config.setSessionIdleTimeout(options.getSessionIdleTimeout());
        config.setStatisticsInterval(config.getStatisticsInterval());
        builder.setConfiguration(config);
    }

    private void setProviderBuilderSpecificSettings(XMemcachedClientBuilder builder, XMemcachedProperties conf) {
        // // 序列化、反序列化
        // if (conf.getTranscoderMaxSize() > 0) {
        //     builder.setTranscoder(new SerializingTranscoder(conf.getTranscoderMaxSize()));
        // } else {
        //     builder.setTranscoder(new SerializingTranscoder());
        // }
        //
        // if (prop.getAuthInfoMap() != null) {
        //     builder.setAuthInfoMap(prop.getAuthInfoMap());
        // }
    }

    // protected void configureClient(XMemcachedClientBuilder builder, XMemcachedClient client,
    //                                XMemcachedProperties conf) {
    //     client.setKeyProvider(this.keyProvider);
    // }

    private void configureClient(XMemcachedClientBuilder builder, MemcachedClient client,
                                 XMemcachedProperties conf) {
        if (builder.getCommandFactory().getProtocol() == Protocol.Kestrel) {
            client.setOptimizeGet(false);
        } else {
            client.setOptimizeGet(conf.isOptimizeGet());
        }

        client.setEnableHeartBeat(conf.isEnableHeartBeat());

        if (conf.getHealConnectionInterval() > 0) {
            client.setHealSessionInterval(conf.getHealConnectionInterval());
        }

        if (conf.getMergeFactor() > 0) {
            client.setMergeFactor(conf.getMergeFactor());
        }

        client.setOptimizeMergeBuffer(conf.isOptimizeMergeBuffer());
        client.setPrimitiveAsString(conf.isPrimitiveAsString());
    }
}
