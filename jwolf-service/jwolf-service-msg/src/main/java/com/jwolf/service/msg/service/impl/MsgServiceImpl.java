package com.jwolf.service.msg.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jwolf.service.msg.api.entity.Msg;
import com.jwolf.service.msg.constant.MsgTypeEnum;
import com.jwolf.service.msg.mapper.MsgMapper;
import com.jwolf.service.msg.service.IMsgService;
import com.jwolf.service.user.api.feign.UserFeginClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author jwolf
 * @since 2021-11-05
 */
@Service
public class MsgServiceImpl extends ServiceImpl<MsgMapper, Msg> implements IMsgService {
    @Autowired
    private UserFeginClient userFeginClient;



    @Override
    @GlobalTransactional
    public boolean insertUserAndMsg() {
        Msg msg = new Msg()
                .setId(IdWorker.getId())
                .setMsg("SEATA测试消息" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .setReceiverId(1L)
                .setType(MsgTypeEnum.Login.getKey())
                .setSenderId(2L);
        this.save(msg);
        boolean isOK = userFeginClient.updateUser(1L);
        //抛出异常，user服务的修改要回滚
        Assert.isTrue(1==2,"更新用户信息失败");
        return true;
    }
}
