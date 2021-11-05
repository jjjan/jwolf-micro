package com.jwolf.common.exception;


import com.jwolf.common.constant.ErrorEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommonException extends RuntimeException {
    private String errorCode;
    private String errorMsg;

    public CommonException(String errorMsg, Throwable e) {
        super(e);
        this.errorCode = ErrorEnum.ERROR.getCode();
        this.errorMsg = errorMsg;
    }

    public CommonException(String errorMsg) {
        this.errorCode = ErrorEnum.ERROR.getCode();
        this.errorMsg = errorMsg;
    }

    public CommonException(ErrorEnum resEnum, Throwable e) {
        super(e);
        this.errorCode = resEnum.getCode();
        this.errorMsg = resEnum.getMsg();
    }

    public CommonException(ErrorEnum resEnum) {
        this.errorCode = resEnum.getCode();
        this.errorMsg = resEnum.getMsg();
    }

}
