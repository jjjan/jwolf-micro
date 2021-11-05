package com.jwolf.service.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jwolf.service.user.api.entity.User;
import com.jwolf.service.user.mapper.UserMapper;
import com.jwolf.service.user.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jwolf
 * @since 2021-11-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
