package com.jwolf.common.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexUtil {

    public static boolean isNumberOrLetter(String str) {
        String regEx = "^[0-9a-zA-Z]{1,}$";
        return str.matches(regEx);
    }


    public static boolean isMobile(String mobiles) {
        if (!StringUtils.hasText(mobiles)) {
            return false;
        }
        Pattern p = Pattern.compile("^1[3456789]\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        boolean flag = false;

        try {
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception var5) {
            flag = false;
        }

        return flag;
    }


}

