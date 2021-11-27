package com.jwolf.jwolfmanage.config.globleexception;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2019-09-27 12:54
 */
@Configuration
public class GlobalExceptionHandlerConfiguration {

    @Bean
    @Profile({"test","prod"})
    public GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
