package com.jwolf.jwolfauth.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.jwolf.common.bean.ResultEntity;
import com.jwolf.jwolfauth.constant.AuthConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "短信验证码")
@RestController
public class CodeController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取access_token与refresh_token
     * http://localhost:9402/oauth/token?mobile=18111295017&code=666666&grant_type=sms_code&client_id=client2&client_secret=secret2
     *
     * @param mobile
     * @return
     */
    @ApiOperation(value = "发送短信验证码")
    @ApiImplicitParam(name = "mobile", example = "18111295017", value = "手机号", required = true)
    @PostMapping("/sms-code")
    public ResultEntity sendSmsCode(String mobile) {
        String code = RandomUtil.randomNumbers(6); // 随机生成6位的验证码
        code = "666666";//写死方便调试
        redisTemplate.opsForValue().set(AuthConstant.SMS_CODE_PREFIX + mobile, code, 10, TimeUnit.MINUTES);
        //省略真正发短信代码;
        return ResultEntity.success();
    }


    /**
     * captcha验证码,依赖用户名密码认证
     * http://localhost:9402/oauth/token?grant_type=captcha&client_secret=secret11&client_id=client1&username=sysuser1&password=123456&validateCode=666666&captchaId=666666
     *
     * @return
     */
    @ApiOperation(value = "获取captcha验证码")
    @PostMapping("/captcha-code")
    public ResultEntity getCaptchaCode() {
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        captcha.createCode();
        String code = captcha.getCode();
        String imageBase64 = captcha.getImageBase64();
        long captchaId = IdWorker.getId();
        code = "666666";
        captchaId = 666666;
        Map<Object, Object> map = MapBuilder.create().put("captchaBase64", imageBase64).put("captchaId", captchaId).build();
        redisTemplate.opsForValue().set(AuthConstant.CAPTCHA_CODE_PREFIX + captchaId, code, 365, TimeUnit.DAYS);
        return ResultEntity.success(map);
    }

}
