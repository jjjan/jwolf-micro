package com.jwolf.service.msg.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jwolf.common.base.entity.BasePageSearch;
import com.jwolf.common.base.entity.ResultEntity;
import com.jwolf.service.msg.api.entity.Msg;
import com.jwolf.service.msg.service.IMsgService;
import com.jwolf.service.user.api.entity.User;
import com.jwolf.service.user.api.feign.UserFeginClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息表 前端控制器
 *
 * @author jwolf
 * @since 2021-11-05
 */
@Tag(name = "消息")
@RestController
@RequestMapping("/msg/msg")
public class MsgController {

    @Autowired
    private IMsgService msgService;
    @Autowired
    private UserFeginClient userFeginClient;

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public ResultEntity
            <Page<Msg>> getPageList(BasePageSearch search) {
        return ResultEntity.success(msgService.page(search.getPage()));
    }


    @ApiOperation(value = "根据id查询")
    @GetMapping("/detail")
    public ResultEntity<Msg> getById(Long id) {
        return ResultEntity.success(msgService.getById(id));
    }


    @ApiOperation(value = "新增")
    @PostMapping("/add")
    public ResultEntity add(@RequestBody Msg msg) {
        boolean isOK = msgService.save(msg);
        return isOK ? ResultEntity.success() : ResultEntity.fail("新增失败,请重试");
    }


    @ApiOperation(value = "批量删除")
    @DeleteMapping("/del")
    public ResultEntity delete(@RequestParam("ids") List
            <String> ids) {
        boolean isOK = msgService.removeByIds(ids);
        return isOK ? ResultEntity.success() : ResultEntity.fail("删除失败,请重试");
    }


    @ApiOperation(value = "更新")
    @PutMapping("/update")
    public ResultEntity update(@RequestBody Msg msg) {
        boolean isOK = msgService.updateById(msg);
        return isOK ? ResultEntity.success() : ResultEntity.fail("更新失败,请重试");
    }

    @ApiOperation(value = "查询用户-feign")
    @GetMapping("/user")
    public ResultEntity<User> getUserByFeign(Long id) {
        return ResultEntity.success(userFeginClient.getUserByFeign(id));
    }




}
