package com.jwolf.service.msg.api.entity;

    import com.baomidou.mybatisplus.annotation.TableName;
    import com.baomidou.mybatisplus.extension.activerecord.Model;
    import com.baomidou.mybatisplus.annotation.Version;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.FieldFill;
    import com.baomidou.mybatisplus.annotation.TableLogic;
    import com.baomidou.mybatisplus.annotation.TableField;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 消息表
    * </p>
*
* @author jwolf
* @since 2021-11-07
*/
    @Data
        @EqualsAndHashCode(callSuper = true)
    @Accessors(chain = true)
    @TableName("t_msg")
    public class Msg extends Model {

    private static final long serialVersionUID = 1L;

    private Long id;

            /**
            * 消息
            */
    private String msg;

            /**
            * 消息类型
            */
    private Integer type;

            /**
            * 发送方ID
            */
    private Long senderId;

            /**
            * 接收方ID
            */
    private Long receiverId;

            @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

            @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

        @TableLogic
    private Integer deleted;

            /**
            * 浏览次数
            */
    private Integer readNum;


}
