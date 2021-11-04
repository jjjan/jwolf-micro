package com.jwolf.common.aop.log;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author jwolf
 * @Date 2021-10-29 22:24
 */
@Getter
@Setter
@Accessors(chain = true)
public class LogBuilder implements Serializable {
    //雪花ID
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JSONField(ordinal = 0)
    private String app; // 微服务名

    @JSONField(ordinal = 2)
    private String module; // 在页面哪个模块

    @JSONField(ordinal = 3) //请求时间
    private long reqTime;

    @JSONField(ordinal = 4)
    private String userId; // 用户ID

    @JSONField(ordinal = 6)
    private String accessType; // 1-PC 0手机

    @JSONField(ordinal = 7)
    private String ip; // 请求IP

    @JSONField(ordinal = 8)
    private String method; // 请求类型GET POST DELETE PUT

    @JSONField(ordinal = 9)
    private String uri; // 请求URI

    @JSONField(ordinal = 10)
    private String params; // 请求参数

    @JSONField(ordinal = 11)
    private String code; // 错误码

    @JSONField(ordinal = 12)
    private Object data; // 返回内容

    @JSONField(ordinal = 13)
    private String msg; // 错误信息
    @JSONField(ordinal = 14)
    private String operationLog; // 操作日志
    @JSONField(ordinal = 15) //耗时
    private long cost;


}
