package top.ylonline.xmemcached.spring.boot.config.type;

/**
 * 协议工场
 *
 * @author YL
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
