package com.jwolf.service.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jwolf.common.base.entity.BasePageSearch;
import com.jwolf.common.base.entity.ResultEntity;
import com.jwolf.service.user.api.entity.User;
import com.jwolf.service.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户表 前端控制器
 *
 * @author jwolf
 * @since 2021-11-04
 */
@Api(tags = {"用户表"})
@RestController
@RequestMapping("/user/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public ResultEntity<Page<User>> getPageList(BasePageSearch search) {
        return ResultEntity.success(userService.page(search.getPage()));
    }


    @ApiOperation(value = "根据id查询")
    @GetMapping("/detail}")
    public ResultEntity<User> getById(Long id) {
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
