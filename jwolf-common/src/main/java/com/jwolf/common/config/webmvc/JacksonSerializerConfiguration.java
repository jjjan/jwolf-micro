package com.jwolf.common.config.webmvc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 全局jackson序列化/反序列化全局设置，可被覆盖
 * 全局Long转String,LocalDateTime LocalDate LocalTime  java.util.Date序列化与反序列化
 * 可以通过实现WebMvcConfigurer接口
 * @author majun
 * @since 2021-11-27
 */
@Configuration
//@Primary
public class JacksonSerializerConfiguration implements WebMvcConfigurer {

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String pattern;
    @Value("${spring.jackson.time-zone:GMT+8}")
    private String timeZone;



    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> cvt : converters) {
            if (cvt instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter converter= (MappingJackson2HttpMessageConverter) cvt;
                enhanceConvertor(converter);
            }

        }

    }

    private void enhanceConvertor(MappingJackson2HttpMessageConverter converter) {
        ObjectMapper objectMapper = converter.getObjectMapper();
        objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //关闭日期序列化为时间戳的功能
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //关闭序列化的时候没有为属性找到getter方法,报错
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //序列化的时候序列对象的所有属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //空值不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //序列化空对象不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //关闭反序列化的时候，没有找到属性的setter报错
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        //反序列化未知属性不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //自定义序列化：key-序列化字段类型,value-序列化所用对象,支持自定义及Jackson自带序列化器
        SimpleModule module = new SimpleModule();
        String[] patternArr = pattern.split(" ");
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern)));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(patternArr[0])));
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(patternArr[1])));
        module.addSerializer(Date.class, new DateSerializer(false,new SimpleDateFormat(pattern)));
        //module.addSerializer(Long.class,ToStringSerializer.instance); // 会将mybatisplus 分页返回的total也转成字符串导致flutter问题
        // 自定义序列化：long长度超过18位则认为是雪花ID,或17位的MySQL的uuid_short(),转为string，否则为数字，
        module.addSerializer(Long.class, new JsonSerializer<Long>() {
            @Override
            public void serialize(Long aLong, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                String str = String.valueOf(aLong);
                if (str.length()>=17) {
                    jsonGenerator.writeString(str);
                }else {
                    jsonGenerator.writeNumber(aLong);
                }
            }
        });
        //自定义反序列化：key-序列化字段类型,value-序列化所用对象,支持自定义及Jackson自带序列化器
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern)));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(patternArr[0])));
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(patternArr[1])));
        module.addDeserializer(Date.class, new DateDeserializers.DateDeserializer(){
            @SneakyThrows
            @Override
            public Date deserialize(JsonParser jsonParser, DeserializationContext ctxt){
                String text = jsonParser.getText().trim();
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                return sdf.parse(text);
            }
        });
        objectMapper.registerModule(module);
    }

}
