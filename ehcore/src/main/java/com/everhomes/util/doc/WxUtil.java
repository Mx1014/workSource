package com.everhomes.util.doc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.configuration.ConfigurationsProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.jasper.tagplugins.jstl.core.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class WxUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxUtil.class);


    private static String URL_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static String URL_IMG_DOWN = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";


    public static String getAccessToken(Integer namespaceId) throws IOException {

        ConfigurationProvider configurationProvider = PlatformContext.getComponent(ConfigurationProvider.class);
        /**
         * access_token String 认证token
         * expires_in Integer 有效时间 秒
         * recevie_time Date 获取认证时间
         */
        String appId = configurationProvider.getValue(namespaceId, "wx.offical.account.appid", "");
        String secret = configurationProvider.getValue(namespaceId, "wx.offical.account.secret", "");
        String accessToken = configurationProvider.getValue(namespaceId, "wx.offical.account.accesstoken","");
        Integer expiresIn = configurationProvider.getIntValue(namespaceId, "wx.offical.account.expiresin", 0);
        Date recevieTime = configurationProvider.getDateValue(namespaceId, "wx.offical.account.recevietime", null);
        if (StringUtils.isNotEmpty(accessToken)) {
            Calendar c = Calendar.getInstance();
            c.setTime(recevieTime);
            c.add(Calendar.SECOND, expiresIn);
            Date expiryTime = c.getTime();
            if (expiryTime.after(new Date())) {
                // token未过期，直接从缓存获取返回
                return accessToken;
            }
        }
        String url = String.format(URL_TOKEN,appId,secret);
        String result = HttpUtils.get(url,null);
        if(result.contains("access_token")) {
            JSONObject resultObj = JSON.parseObject(result);
            accessToken = resultObj.getString("access_token");
            expiresIn = resultObj.getInteger("expires_in");
            recevieTime = new Date();
            configurationProvider.setValue(namespaceId, "wx.offical.account.accesstoken", accessToken);
            configurationProvider.setIntValue(namespaceId, "wx.offical.account.expiresin", expiresIn);
            configurationProvider.setDateValue(namespaceId, "wx.offical.account.recevietime", recevieTime);
        }
        return accessToken;
    }

    public static UploadCsFileResponse DownloadImage(String mediaId,Integer namespaceId) throws Exception {

        if(StringUtils.isEmpty(mediaId)){
            LOGGER.info("invild media_id");
            return null;
        }
        String token = "";
        try {
            token = WxUtil.getAccessToken(namespaceId);
        } catch (IOException e) {
            LOGGER.error("Error occur while getting access_token");
            e.printStackTrace();
            throw new IOException("");
        }

        String urlStr = String.format(URL_IMG_DOWN, token, mediaId);
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String contentType = conn.getHeaderField("Content-Type");
        if (contentType.contains("image")) {
            //从微信下载图片并上传到自己的服务器
            String disposition = conn.getHeaderField("Content-disposition");
            //get the filename from the Content-Disposition header
            String fileName = disposition.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");

            InputStream in = conn.getInputStream();

            String contentServerToken = WebTokenGenerator.getInstance().toWebToken(User.SYSTEM_USER_LOGIN);
            ContentServerService contentServerService = PlatformContext.getComponent(ContentServerService.class);

            UploadCsFileResponse result = contentServerService.uploadFileToContentServer(in,fileName,contentServerToken);
            return  result;
        }

        return null;
    }

    public static void main(String[] args) {
        try {
//            Map<String,Object> token = WxUtil.getAccessToken();
//            System.out.println("-------------------------------------- access token --------------------------------------");
//            System.out.println(token.get("access_token").toString());
            WxUtil.DownloadImage("R9fWB8i0zMZbaUApBJZqWZAnC-mpP206extmKECtkumeKhSQGsz7Iwg3sD6xVK8W",9);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
