package com.jwolf.service.user.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jwolf.common.bean.BasePageSearch;
import com.jwolf.common.bean.ResultEntity;
import com.jwolf.service.user.api.entity.User;
import com.jwolf.service.user.config.SentinelHandler;
import com.jwolf.service.user.service.IUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户表 前端控制器
 *
 * @author jwolf
 * @since 2021-11-04
 */
@Tag(name = "用户表")
@RestController
@RequestMapping("/user/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResultEntity<Page<User>> getPageList(BasePageSearch search) {
        return ResultEntity.success(userService.page(search.getPage()));
    }

    @Operation(summary = "用户详情")
    @GetMapping("/detail")
    @SentinelResource(value = "testsentinel",
            blockHandler = "exceptionHandler",
            blockHandlerClass = SentinelHandler.class,
            fallback = "fallbackHandler",
            fallbackClass = SentinelHandler.class)
    public ResultEntity<User> getById(@Parameter(description="用户id")Long id) {
        int a=1/0;
        return ResultEntity.success(userService.getById(id));
    }


    @ApiOperation(value = "新增")
    @PostMapping("/add")
    public ResultEntity add(@RequestBody User user) {
        boolean isOK = userService.save(user);
        return isOK ? ResultEntity.success() : ResultEntity.fail("新增失败,请重试");
    }


    @ApiOperation(value = "批量删除")
    @DeleteMapping("/del")
    public ResultEntity delete(@RequestParam("ids") List<String> ids) {
        boolean isOK = userService.removeByIds(ids);
        return isOK ? ResultEntity.success() : ResultEntity.fail("删除失败,请重试");
    }


    @ApiOperation(value = "更新")
    @PutMapping("/update")
    public ResultEntity update(@RequestBody User user) {
        boolean isOK = userService.updateById(user);
        return isOK ? ResultEntity.success() : ResultEntity.fail("更新失败,请重试");
    }


}
