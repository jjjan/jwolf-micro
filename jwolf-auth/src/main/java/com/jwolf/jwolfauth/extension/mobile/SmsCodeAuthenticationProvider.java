package com.jwolf.jwolfauth.extension.mobile;

import cn.hutool.core.util.StrUtil;
import com.jwolf.jwolfauth.constant.AuthConstant;
import com.jwolf.jwolfauth.core.MemberUserDetailsServiceImpl;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashSet;

/**
 * 短信验证码认证授权提供者
 *
 * @author <a href="mailto:xianrui0365@163.com">xianrui</a>
 * @date 2021/9/25
 */
@Data
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private RedisTemplate redisTemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        String code = (String) authenticationToken.getCredentials();
        String codeKey = AuthConstant.SMS_CODE_PREFIX + mobile;
        String correctCode = (String) redisTemplate.opsForValue().get(codeKey);
        if (StrUtil.isBlank(correctCode) || !code.equals(correctCode)) {
            //throw new CommonException("验证码不正确");
        }
        // 比对成功删除缓存的验证码
        redisTemplate.delete(codeKey);
        UserDetails userDetails = ((MemberUserDetailsServiceImpl) userDetailsService).loadUserByMobile(mobile);
        SmsCodeAuthenticationToken result = new SmsCodeAuthenticationToken(userDetails, authentication.getCredentials(), new HashSet<>());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
