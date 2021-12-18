package com.jwolf.common.config.webmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwolf.common.util.JsonUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 全局jackson序列化/反序列化全局设置，可被覆盖
 * 全局Long转String,LocalDateTime LocalDate LocalTime  java.util.Date序列化与反序列化
 * 可以通过实现WebMvcConfigurer接口
 *
 * @author majun
 * @since 2021-11-27
 */
@Configuration
//@Primary
public class JacksonSerializerConfiguration implements WebMvcConfigurer {



    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> cvt : converters) {
            if (cvt instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter converter = (MappingJackson2HttpMessageConverter) cvt;
                enhanceConvertor(converter);
            }

        }

    }

    private void enhanceConvertor(MappingJackson2HttpMessageConverter converter) {
        ObjectMapper objectMapper = converter.getObjectMapper();
        JsonUtils.configObjectMapper(objectMapper);
    }


}
