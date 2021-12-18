package com.jwolf.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author: 波波烤鸭
 **/
public class JsonUtils {

    public static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final String pattern = "yyyy-MM-dd HH:mm:ss";
    private static final String timeZone = "GMT+8";


    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("json序列化出错：" + obj, e);
            return null;
        }
    }

    public static <T> T toBean(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static <E> List<E> toList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * 全局使用该配置，redis kv序列化反序列化,覆盖默认jackson配置也使用该objectmapper配置
     *
     * @param objectMapper
     */
    public static void configObjectMapper(ObjectMapper objectMapper) {
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
        module.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(pattern)));
        //module.addSerializer(Long.class,ToStringSerializer.instance); // 会将mybatisplus 分页返回的total也转成字符串导致flutter问题
        // 自定义序列化：long长度超过18位则认为是雪花ID,或17位的MySQL的uuid_short(),转为string，否则为数字，
        module.addSerializer(Long.class, new JsonSerializer<Long>() {
            @Override
            public void serialize(Long aLong, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                String str = String.valueOf(aLong);
                if (str.length() >= 17) {
                    jsonGenerator.writeString(str);
                } else {
                    jsonGenerator.writeNumber(aLong);
                }
            }
        });
        //自定义反序列化：key-序列化字段类型,value-序列化所用对象,支持自定义及Jackson自带序列化器
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern)));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(patternArr[0])));
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(patternArr[1])));
        module.addDeserializer(Date.class, new DateDeserializers.DateDeserializer() {
            @SneakyThrows
            @Override
            public Date deserialize(JsonParser jsonParser, DeserializationContext ctxt) {
                String text = jsonParser.getText().trim();
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                return sdf.parse(text);
            }
        });
        objectMapper.registerModule(module);


    }
}
