package com.jwolf.jwolfgateway.filter;

import util.YmlUtils;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2021-10-29 22:57
 */
public class Test {
    public static void main(String[] args) {

       String v1=YmlUtils.getConfig("server.port","222");
        String v2=YmlUtils.getConfig("serverxx.port");
    }
}
