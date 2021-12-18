package com.jwolf.common.constant;

import com.jwolf.common.exception.CommonException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnum {
    ERROR("1", "操作失败"),
    UNKNOWN_ERROR("9999", "操作失败,系统错误!"),
    ILLEGAL_ERROR("8888", "非法请求!"),
    ACCESS_LIMMIT_ERROR("0403", "权限不足"),
    NOT_FOUND_ERROR("0404", "未查询到数据"),
    DELETE_ERROR("0500", "删除失败,请重试"),
    INSERT_ERROR("0501", "新增失败"),
    UPDATE_ERROR("0502", "更新失败"),
    REPEAT_ERROR("0503", "请勿重复操作"),
    REMOTE_CALL_ERROR("0504", "系统异常"),
    TIMEOUT_ERROR("0505", "请求超时"),
    THIRD_INTERFACE_CALL_ERROR("0506", "第三方接口请求失败"),
    LOGIN_ERROR("0507", "用户名或密码错误"),
    LOGIN_EXPIRATION("0520", "登录已过期,请重新登录"),
    UNLOGIN_ERROR("0521", "请先登录再进行操作"),
    ARGS_VERIFY_ERROR("0508", "参数验证未通过"),
    UPLOAD_ERROR("0509", "上传失败,请重试！"),
    IO_ERROR("0510", "IO异常"),
    ZUUL_NOT_UP_ERROR("0511", "网关服务发现异常——服务未启动或暂时未注册到注册中心"),
    ZUUL_TIMEOUT_ERROR("0512", "网关请求微服务异常——请求超时"),
    ZUUL_BREAKER_ERROR("0513", "网关熔断器被打开——服务熔断"),
    FILE_TYPE_ERROR("0514", "文件格式不正确"),
    SMS_ERROR("0515", "短信发送失败，请联系管理员！"),
    DOWNLOAD_ERROR("0516", "下载失败"),
    ;
    private String code;
    private String msg;

    public static ErrorEnum getResponseEnumByCode(String code) {
        ErrorEnum[] values = ErrorEnum.values();
        for (ErrorEnum resEnum : values) {
            if (resEnum.getCode().equals(code)) {
                return resEnum;

            }
        }
        throw new CommonException("未定义的错误类型");
    }


}
