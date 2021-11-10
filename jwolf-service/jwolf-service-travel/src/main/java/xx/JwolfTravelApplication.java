package xx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = RedisAutoConfiguration.class)
//@EnableSwagger2
//@MapperScan("com.jwolf.service.user.mapper")
@EnableDiscoveryClient(autoRegister = false) //暂不启用nacos

public class JwolfTravelApplication {

    public static void main(String[] args) {

        SpringApplication.run(JwolfTravelApplication.class, args);
    }


}
