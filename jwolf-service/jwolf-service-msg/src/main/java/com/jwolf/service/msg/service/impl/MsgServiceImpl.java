package com.jwolf.service.msg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jwolf.service.msg.api.entity.Msg;
import com.jwolf.service.msg.mapper.MsgMapper;
import com.jwolf.service.msg.service.IMsgService;
import org.springframework.stereotype.Service;

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

}
