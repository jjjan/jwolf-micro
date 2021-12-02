package com.jwolf.common.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author jwolf
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultEntity<T> {

    private String code;
    private String msg;
    private T data;


    public static ResultEntity success() {
        return new ResultEntity("200", "请求成功", null);
    }

    public static ResultEntity success(Object data) {
        return new ResultEntity("200", "请求成功", data);
    }

    public static ResultEntity success(Object data, String msg) {
        return new ResultEntity("200", msg, data);
    }

    public static ResultEntity fail(String code, String message) {
        return new ResultEntity(code, message, null);
    }

    public static ResultEntity fail(String message) {
        return new ResultEntity("5XX", message, null);
    }

}
