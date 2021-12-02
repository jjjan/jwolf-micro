package com.jwolf.common.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author jwolf
 * @Date 2021-10-29 22:24
 */
@Getter
@Setter
//不序列化 空值，否则Vue级联选择组件不能正常使用
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CascadeVO {
    public String value;
    public String label;
    public List children;

}
