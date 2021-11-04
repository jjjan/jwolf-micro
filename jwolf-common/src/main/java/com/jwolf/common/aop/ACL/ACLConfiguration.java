package com.jwolf.common.aop.ACL;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/***
 * @author jwolf
 * @Date 2021-10-29 22:24
 */
@Configuration
public class ACLConfiguration {
    @Bean
    @Profile({"test", "dev", "prod"})
    public ACLAspect getACLAspect() {
        return new ACLAspect();
    }
}
