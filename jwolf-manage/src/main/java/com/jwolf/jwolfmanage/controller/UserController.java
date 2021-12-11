package com.jwolf.jwolfmanage.controller;

import com.jwolf.common.bean.ResultEntity;
import com.jwolf.jwolfmanage.bean.vo.UserVO;
import com.jwolf.jwolfmanage.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


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


    /**
     * 前端请求接口时如果返回的是text/html的auth工程的SSO登录页面，前端location.href请求该接口
     * 该接口再重定向到sso登录，登录后回调该接口，但该页面的地址是http://192.168.154.143:8888/jwolf/manage/user/login 与开发环境前端地址
     * http://192.168.154.143:9528不同域，cookie无法传递，需要通过nginx将两者进行转发
     *
     * #后台管理后端
     * server {
     *    listen       80;
     *    location ~ ^/jwolf/manage {
     *     proxy_pass http://192.168.154.143:8888;
     *     }
     * #后台管理前端
     *   location / {
     *root   /home/manage;
     *index  index.html;
     *    }
     * }
     * @return
     */
    @ApiOperation(value = "【登录中】中转临时页面")
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }
}
