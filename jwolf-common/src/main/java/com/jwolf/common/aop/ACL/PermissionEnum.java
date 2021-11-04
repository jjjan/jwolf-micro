package com.jwolf.common.aop.ACL;


import com.jwolf.common.constant.ErrorEnum;
import com.jwolf.common.exception.CommonException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jwolf
 * @Date 2021-10-29 22:24
 */

@AllArgsConstructor
@Getter
public enum PermissionEnum {


    USER_CUD(100, "☆增删改(含权限)", "XX", 0),

    ;

    private int permissionCode;//权限码
    private String descr;//描述
    private String moduleName;//所属模块
    private int sort;//排序字段

    public static String getPermissionDescrByCode(int code) {
        for (PermissionEnum enumItem : PermissionEnum.values()) {
            if (enumItem.permissionCode == code) {
                return enumItem.getDescr();
            }
        }
        throw new CommonException(ErrorEnum.UNKNOWN_ERROR);
    }


}


