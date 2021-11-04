package com.jwolf.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
public class MonitorUtils {
    private static String KEYWORD = "正式环境监控报警:";//信息内容需要包含web端钉钉机器人设置的关键词才能正常发送
    private static String PROFILE = YmlUtils.getConfig("spring.profiles.active");//正式环境prod，测试环境test
    private static String DINGDING_URL = "https://oapi.dingtalk.com/robot/send?access_token=";

    //https://oapi.dingtalk.com/robot/send?access_token=94222a74e113286fb28fae1db6cc3c60aef505e8a1c
    static {
        String webhookToken = YmlUtils.getConfig("dingding.webhookToken");
        if (!StringUtils.hasText(webhookToken)) {
            webhookToken = "xxxxxxxxxxx";
        }
        DINGDING_URL += webhookToken;
    }

    public static void sendDingdingMsg(String msgContent) {
        if (!"prod".equals(PROFILE)) {
            return;
        }
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(DINGDING_URL);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        String textMsg = "{ \"msgtype\": \"text\", \"text\": {\"content\": \"" + KEYWORD + msgContent + "\"}}";
        StringEntity se = new StringEntity(textMsg, "utf-8");
        httppost.setEntity(se);
        try {
            HttpResponse response = httpclient.execute(httppost);
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            log.info("发送钉钉监控报警结果:{}", result);
        } catch (IOException e) {
            log.error("发送钉钉监控报警出现IO异常", e);

        }

    }

    public static void main(String[] args) {
        sendDingdingMsg("生产环境异常");
    }

}
