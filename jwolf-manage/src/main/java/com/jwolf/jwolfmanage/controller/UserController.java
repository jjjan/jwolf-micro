package com.jwolf.jwolfmanage.controller;

import com.jwolf.common.bean.ResultEntity;
import com.jwolf.jwolfmanage.bean.vo.UserVO;
import com.jwolf.jwolfmanage.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Api(tags = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @ApiOperation(value = "获取当前登陆的用户信息")
    @GetMapping("/me")
    public ResultEntity<UserVO> getCurrentUser() {
        UserVO sysUser = TokenUtil.getCurrentSysUser();
        //因按钮权限内容可能较多，故auth服务没将permission加入JWT,避免JWT过大，这里如果前端要根据权限做按钮是否显示等细粒度控制则需要
        //根据JWT解析到的角色从DB查询权限,返回给前端存入sessionStorage等，如果仅后端控制则使用security的权限注解即可
        sysUser.setPerms(null);
        return ResultEntity.success(sysUser);
    }
}
