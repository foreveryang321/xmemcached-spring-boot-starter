package top.ylonline.xmemcached;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author YL
 */
@SpringBootApplication
public class App {
    /**
     * Common
     */
    private static SpringApplicationBuilder configureSpringBuilder(SpringApplicationBuilder builder) {
        return builder.sources(App.class);
    }

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
                    // ignore
                    break;
                }
            }
        }
    }
}
