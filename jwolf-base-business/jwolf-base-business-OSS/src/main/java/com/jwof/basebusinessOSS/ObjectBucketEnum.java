package com.jwof.basebusinessOSS;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2021-12-18 00:19
 */
@AllArgsConstructor
@Getter
public enum ObjectBucketEnum {
    USER_ICON("usericon", "用户头像"),
    IDCARD1("idcard1", "身份证正面"),
    IDCARD2("idcard2", "身份证反面");
    private String bucketName;
    private String remark;
}
