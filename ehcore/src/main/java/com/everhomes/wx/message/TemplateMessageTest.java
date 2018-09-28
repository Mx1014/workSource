package com.everhomes.wx.message;

import com.everhomes.http.HttpUtils;
import com.everhomes.util.StringHelper;
import com.everhomes.wx.WeChatServiceImpl;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TemplateMessageTest {
    public static void main(String[] args) {
        TemplateMessageTest test = new TemplateMessageTest();
        test.sendTemplateMessage(null, null, null);
    }

    private static String sendApiUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    private static String APPID = "wxc9793912e431c111";
    private static String SECRET = "c39f9f48b56d419984a98d3acd26ead4";

    private static String OPENID = "oJ9Ss1Lthx50aEcUQsHjP0HqFclc";
    private static String TEMPLATEID = "twO8yjBElJdltKiqB-K-q1nbX2zHwxhLrHNLdXyE-9k";

    private static String DETAILID = "http://music.163.com/song/1299594413/?userid=267983715";

    public void sendTemplateMessage(String openId, String templateId, HashMap<String, Item> params){
        openId = OPENID;
        templateId = TEMPLATEID;

        params = new HashMap<>();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());

        TemplateMessage tm = new TemplateMessage();
        tm.setTouser(openId);
        tm.setTemplate_id(templateId);
        tm.setTopcolor("#743A3A");
        tm.setUrl(DETAILID);
        tm.add("nickname","萌萌", "#008000");
        tm.add("location1","武汉理工大学", "#008000");
        tm.add("location2","第二师范", "#008000");

        String json = tm.build();

        System.out.println(json);

        String jsonResult = null;
        try {

            TreeMap<String,String> params_1 = new TreeMap<>();
            params_1.put("grant_type", "client_credential");
            params_1.put("appid", APPID);
            params_1.put("secret", SECRET);
            String response_1 = HttpUtils.get("https://api.weixin.qq.com/cgi-bin/token" , params_1);
            Map map_1 = (Map) StringHelper.fromJsonString(response_1, Map.class);
            String accessToken = (String) map_1.get("access_token");
            jsonResult = HttpUtils.postJson(sendApiUrl + accessToken, json, 30, HTTP.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(jsonResult);

    }
}
