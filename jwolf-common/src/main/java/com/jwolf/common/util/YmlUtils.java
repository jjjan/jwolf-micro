package com.jwolf.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * yml文件工具类,注意该工具类不能获取nacos远程配置中心的配置
 */
@Slf4j
public class YmlUtils {
    private static final Map<String, String> confMap = new HashMap(256);

    static {
        ClassLoader classLoader = YmlUtils.class.getClassLoader();
        Yaml yaml = new Yaml();
        InputStream applicationIn = classLoader.getResourceAsStream("application.yml");
        fillConfMap(yaml.loadAs(applicationIn, Map.class), confMap, null);
        IOUtils.closeQuietly(applicationIn);
        InputStream bootstrapIn = classLoader.getResourceAsStream("bootstrap.yml");
        if (bootstrapIn != null) {
            fillConfMap(yaml.loadAs(bootstrapIn, Map.class), confMap, null);
            IOUtils.closeQuietly(bootstrapIn);
        }
        String currentProfile = confMap.get("spring.profiles.active");
        InputStream currentProfileIn = classLoader.getResourceAsStream(String.format("application-%s.yml", currentProfile));
        fillConfMap(yaml.loadAs(currentProfileIn, Map.class), confMap, null);
        IOUtils.closeQuietly(currentProfileIn);
    }

    private static void fillConfMap(Map<String, Object> sourceMap, Map<String, String> destinationMap, String key) {
        sourceMap.forEach((key1, v) -> {
            String k = key != null ? key + "." + key1 : key1;
            if (v instanceof Map) {
                fillConfMap((Map) v, destinationMap, k);
            } else {
                destinationMap.put(k, String.valueOf(v));
            }
        });

    }

    public static String getConfig(String key, String defualtValue) {
        String value = confMap.get(key);
        return value != null && !"".equals(value.trim()) ? value : defualtValue;
    }

    public static String getConfig(String key) {
        return confMap.get(key);
    }

    public static void main(String[] args) {
        String v1 = YmlUtils.getConfig("server.port", "222");
        String v2 = YmlUtils.getConfig("serverxx.port");
    }

}