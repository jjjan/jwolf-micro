package com.jwolf.bigdata;

import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ApplicationObjectSupport;


@SpringBootApplication
public class SparkApplication extends ApplicationObjectSupport {

    @SneakyThrows
    public static void main(String[] args) {
        // 不启动spring上下文,直接跑Spark task
        //SpringApplication.run(SparkApplication.class, args);
        SparkmlSvmLrTest.main(args);
    }

}

