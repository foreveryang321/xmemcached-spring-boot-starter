package cn.tisson.xmemcached.spring.boot.config.type;

/**
 * 协议工场
 *
 * @author Created by YL on 2018/11/3
 */
public enum CommandFactory {
    /**
     * {@link net.rubyeye.xmemcached.command.TextCommandFactory{}
     */
    Text,
    /**
     * {@link net.rubyeye.xmemcached.command.BinaryCommandFactory}
     */
    Binary,
    /**
     * {@link net.rubyeye.xmemcached.command.KestrelCommandFactory}
     */
    Kestrel
}
