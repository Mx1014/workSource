package com.everhomes.util.doc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.http.HttpUtils;
import org.apache.jasper.tagplugins.jstl.core.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        } else {
            Map<String,String> TokenParams = new HashMap<>();
            TokenParams.put("corpid",CORP_ID);
            TokenParams.put("corpsecret",CORP_SECRET);
            String result = HttpUtils.get(URL_TOKEN,TokenParams);
            if(result.contains("access_token")) {
                JSONObject resultObj = JSON.parseObject(result);
                resultObj.get("access_token");
                resultObj.get("expires_in");
            }
        }
        return null;
    }

    public File DownloadImage(String corpId, String corpSecret, String mediaId){




        Map<String,String> ImgParams = new HashMap<>();
//        ImgParams.put("access_token","");
        ImgParams.put("media_id",mediaId);


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
