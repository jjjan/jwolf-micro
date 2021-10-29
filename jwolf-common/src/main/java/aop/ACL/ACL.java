package aop.ACL;

import java.lang.annotation.*;

/**
 * 访问控制注解
 *
 * @author jwolf
 * @Date 2021-10-29 22:24
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ACL {
    PermissionEnum[] value() default {};                      //访问权限校验

    boolean mustAdmin() default false;                         //必须为管理员

    int rateLimit() default 0;                                 //接口请求限流，秒

    boolean enableUserLevelRateLimit() default false;          //是否启用用户级别的速率限制

    boolean mustLogin() default true;                           //是否必须登录
    int countLimit() default Integer.MAX_VALUE;                 //访问次数校验
}
