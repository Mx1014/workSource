package com.everhomes.util.doc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.http.HttpUtils;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.apache.jasper.tagplugins.jstl.core.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WxUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxUtil.class);

    private static String CORP_ID = "";
    private static String CORP_SECRET = "";

    private static String URL_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
    private static String URL_IMG_DOWN = "https://qyapi.weixin.qq.com/cgi-bin/media/get";
    /**
     * access_token String 认证token
     * expires_in Integer 有效时间 秒
     * recevie_time Date 获取认证时间
     */
    private static Map<String,Object> ACCESS_TOKEN = new HashMap<>();

    public Map<String, Object> getAccessToken() throws IOException {
        if (!ACCESS_TOKEN.isEmpty()) {
            Date recevieTime = (Date) ACCESS_TOKEN.get("recevie_time");
            Integer expires = (Integer) ACCESS_TOKEN.get("expires_in");
            Calendar c = Calendar.getInstance();
            c.setTime(recevieTime);
            c.add(Calendar.SECOND, expires);
            recevieTime = c.getTime();
            if (recevieTime.after(new Date())) {
                // token未过期，直接从缓存获取返回
                return ACCESS_TOKEN;
            }
        }
        Map<String,String> TokenParams = new HashMap<>();
        TokenParams.put("corpid",CORP_ID);
        TokenParams.put("corpsecret",CORP_SECRET);
        String result = HttpUtils.get(URL_TOKEN,TokenParams);
        if(result.contains("access_token")) {
            JSONObject resultObj = JSON.parseObject(result);
            ACCESS_TOKEN.put("access_token",resultObj.get("access_token"));
            ACCESS_TOKEN.put("expires_in",resultObj.get("expires_in"));
            ACCESS_TOKEN.put("recevie_time",new Date());
        }
        return ACCESS_TOKEN;
    }

    public File DownloadImage(String mediaId) throws Exception {

        if(StringUtils.isEmpty(mediaId)){
            LOGGER.info("invild media_id");
            return null;
        }
        try {
            this.getAccessToken();
        } catch (IOException e) {
            LOGGER.error("Error occur while getting access_token");
            e.printStackTrace();
            throw new IOException("");
        }

        Map<String,String> ImgParams = new HashMap<>();
        ImgParams.put("access_token",(String) ACCESS_TOKEN.get("access_token"));
        ImgParams.put("media_id",mediaId);
        String result = HttpUtils.get(URL_IMG_DOWN,ImgParams);

//        ResponseData result = ResponseData.success(ResponseCode.SUCCESS_PHOTO_CREATE);

//        String accessToken = wechatOAuth2Service.getAccessToken();
//        String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
//        url = String.format(url, accessToken, mediaIds);
//
//        //去下载微信的图片资源
//        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
//        byte[] fileContent = response.getBody();
//
//        MediaType contentType = response.getHeaders().getContentType();
//        if (contentType.getType().contains("image")) {
//            //从微信下载图片并上传到自己的服务器
//            String disposition = response.getHeaders().getFirst("Content-disposition");
//            String fileName = disposition.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");//get the filename from the Content-Disposition header
//
//            String tempFileName = "/tmp/" + fileName;
//
        return null;
    }
}
