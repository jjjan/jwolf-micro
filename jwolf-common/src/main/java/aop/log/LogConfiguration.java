package aop.log;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author jwolf
 * @Date 2021-10-29 22:24
 */
@Configuration
public class LogConfiguration {


    @Bean
    @Profile({"test", "dev", "prod"})
    public LogAspect getLogAspect() {
        return new LogAspect();
    }


    @Bean
    @Profile({"test", "dev", "prod"})
    public LogLevelAutoRefreshListener createLogLevelListener() {
        return new LogLevelAutoRefreshListener();
    }

}
