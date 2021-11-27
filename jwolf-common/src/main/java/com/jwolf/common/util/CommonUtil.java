package com.jwolf.common.util;

import com.jwolf.common.entity.ReqUser;
import com.jwolf.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jwolf
 * @Date 2021-10-29 22:24
 */
@Slf4j
public class CommonUtil {

    /**
     * 获取当前的userId
     *
     * @param request
     */
    public static String getCurrentUser(HttpServletRequest request) throws CommonException {
        String userId = request.getHeader("zuul_user_id");
        return userId != null ? userId : TokenUtils.getUserId("");

    }

    public static String getCurrentUser() throws CommonException {
        return getCurrentUser(getHttpServletRequest());
    }

    /**
     * 获取当前的entpId
     */
    public static String getCurrentEntpId(HttpServletRequest request) throws CommonException {
        String userId = request.getHeader("zuul_entp_id");
        return userId != null ? userId : TokenUtils.getUserId("");
    }

    public static String getCurrentEntpId() throws CommonException {
        return getCurrentEntpId(getHttpServletRequest());
    }

    public static ReqUser getReqUser() throws CommonException {
        return getReqUser();
    }


    /**
     * 获取访问类型1-PC 0-手机
     */
    public static String getAccessType(HttpServletRequest request) throws CommonException {
        String accessType = request.getHeader("zuul_access_type");
        return accessType != null ? accessType : TokenUtils.getUserId("");

    }

    public static String getAccessType() throws CommonException {
        return getAccessType(getHttpServletRequest());

    }


    private static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    @Deprecated  //nginx转发的真实IP可以从header【X-Real-IP 或X-Forwarded-For】获取，zuul转发后的真实IP从header【X-FORWARDED-FOR】获取，必须大写
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}