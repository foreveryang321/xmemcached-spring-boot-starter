package cn.tisson.xmemcached.spring.boot;

import cn.tisson.xmemcached.spring.boot.annotation.EnableXMemcachedConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableXMemcachedConfiguration
public class App {
    /**
     * Common
     */
    private static SpringApplicationBuilder configureSpringBuilder(SpringApplicationBuilder builder) {
        // builder.listeners(new EnvironmentPreparedEventListener());
        return builder.sources(App.class);
    }

    // /**
    //  * for WAR deploy
    //  */
    // @Override
    // protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    //     return configureSpringBuilder(builder);
    // }

    /**
     * for JAR deploy
     */
    public static void main(String[] args) {
        configureSpringBuilder(new SpringApplicationBuilder())
                .run(args);
        synchronized (App.class) {
            while (true) {
                try {
                    App.class.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
