package com.jwolf.common.constant;

import com.jwolf.common.exception.CommonException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jwolf
 * @date
 */
@AllArgsConstructor
@Getter
public enum SmsEnum {
    LOGIN( "SMS_168820407", "登录短信模板"),
    ;
    private String templateCode;//短信模板号
    private String descr;//描述

    /**
     * 根据短信类型获取短信模板号
     *
     * @param smstype
     * @return
     */
    public static String getSmsTemplateCodeByType(SmsEnum smstype) {
        SmsEnum[] values = SmsEnum.values();
        for (SmsEnum smsEnum : values) {
            if (smsEnum.equals(smstype)) {
                return smsEnum.getTemplateCode();

            }
        }
        throw new CommonException("未定义该短信类型");
    }

}
