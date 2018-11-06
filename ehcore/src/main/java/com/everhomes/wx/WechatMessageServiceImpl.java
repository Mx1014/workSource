package com.everhomes.wx;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.http.HttpUtils;

import com.everhomes.messaging.MessagingServiceImpl;
import com.everhomes.rest.user.NamespaceUserType;
import com.everhomes.rest.wx.MessageItem;
import com.everhomes.rest.wx.TemplateMessage;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WechatMessageServiceImpl implements WeChatMessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WechatMessageServiceImpl.class);


    @Autowired
    private WeChatService weChatService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    private static String SEND_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    // private static String DEFAULT_TEMPLATEID = "JnTt-ce69Wlie-o8nv4Jhl3CKA0pXaageIsr4aJiWCk";
    private static String DEFAULT_TOPCOLOR = "#F2C500";

//    private static String APPID = "wxc9793912e431c111";
//    private static String SECRET = "c39f9f48b56d419984a98d3acd26ead4";




    //for test

//    public static void main(String[] args) {
//
//
//        String userOpenId = "oJ9Ss1C1a1NdjowyhjKpwoRCqNW8";
//        String templateId = "ZSo5OQFgg_EmamIsUC-JaPZTJtqCkekNc_oAYP7th6c";
//        String topColor = "#F2C500";
//
//        String routerUrl = "http://music.163.com/song/1299594413/?userid=267983715";
//
//        HashMap<String, Item> params = new HashMap<>();
//
//        params.put("first", new Item("萌萌", "#008000"));
//        params.put("keyword1", new Item("武汉理工", "#008000"));
//        params.put("keyword2", new Item("湖北第二师范", "#008000"));
////        params.put("keyword3", new Item("国庆期间", "#008000"));
////        params.put("remark", new Item("点击查看详情", "#008000"));
//
//
//        WechatMessageServiceImpl test = new WechatMessageServiceImpl();
//        test.sendTemplateMessage(userOpenId, templateId, topColor, params, routerUrl);
//    }



    /**
     *
     * @param userIds 用户ID
     * @param message 消息
     * @param routerUrl 路由
     */
    @Override
    public void sendTemplateMessage(List<Long> userIds, String title, String message, String routerUrl){
        if(userIds == null || userIds.size() == 0 || message == null){
            LOGGER.error("parameter error userIds={}, message={}", userIds, message);
            return;
        }


        if(StringUtils.isEmpty(title)){
            title = "系统消息";
        }

        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=sdf.format(new Date());

        HashMap<String, MessageItem> params = new HashMap<>();


//        {{first.DATA}}
//        业务：{{keyword1.DATA}}
//        时间：{{keyword2.DATA}}
//        提醒内容：{{keyword3.DATA}}
//        处理建议：{{keyword4.DATA}}
//        {{remark.DATA}}

        //params.put("first", new MessageItem(title, "#008000"));
        params.put("keyword1", new MessageItem(title, "#008000"));
        params.put("keyword2", new MessageItem(time, "#008000"));
        params.put("keyword3", new MessageItem(message, "#008000"));
        params.put("keyword4", new MessageItem("查看详情", "#008000"));
        //params.put("remark", new MessageItem("remark, detail", "#008000"));


        String defaultTemplateId = configProvider.getValue(UserContext.getCurrentNamespaceId(),"wx.default.template.id", "");

        if(defaultTemplateId  == null){
            LOGGER.error("can not find wx.default.template.id ");
            return;
        }

        for(Long userId: userIds){
            User user = userProvider.findUserById(userId);
            if(user == null || NamespaceUserType.fromCode(user.getNamespaceUserType()) != NamespaceUserType.WX || StringUtils.isEmpty(user.getNamespaceUserToken())){
                LOGGER.error("this user is not wechatUser, user={}", user);
                continue;
            }
            sendTemplateMessage(user.getNamespaceUserToken(), defaultTemplateId, DEFAULT_TOPCOLOR, params, routerUrl);
        }
    }


    /**
     *
     * @param userOpenId 用户openID
     * @param templateId 模板消息Id
     * @param topColor 消息第一行内容颜色
     * @param params 参数（键值对）
     * @param routerUrl 路由
     */
    @Override
    public void sendTemplateMessage(String userOpenId, String templateId, String topColor, HashMap<String, MessageItem> params, String routerUrl){


        LOGGER.info("sendTemplateMessage start userOpenId={}", userOpenId);

        TemplateMessage tm = new TemplateMessage();
        tm.setTouser(userOpenId);
        tm.setTemplate_id(templateId);
        tm.setTopcolor(topColor);
        tm.setUrl(routerUrl);

        if(params != null){
            for (Map.Entry<String, MessageItem> entry: params.entrySet()){
                tm.add(entry.getKey(), entry.getValue());
            }
        }


        String json = tm.build();

        System.out.println(json);

        String jsonResult = null;
        try {

            //for test start
//            TreeMap<String,String> params_1 = new TreeMap<>();
//            params_1.put("grant_type", "client_credential");
//            params_1.put("appid", APPID);
//            params_1.put("secret", SECRET);
//            String response_1 = HttpUtils.get("https://api.weixin.qq.com/cgi-bin/token" , params_1);
//            Map map_1 = (Map) StringHelper.fromJsonString(response_1, Map.class);
//            String accessToken = (String) map_1.get("access_token");
            //for test end



            String accessToken = weChatService.getAccessToken();

            LOGGER.info("sendTemplateMessage info url={}, jsonData={}", SEND_TEMPLATE_URL + accessToken, json);

            jsonResult = HttpUtils.postJson(SEND_TEMPLATE_URL + accessToken, json, 30, HTTP.UTF_8);

        } catch (IOException e) {
            LOGGER.info("sendTemplateMessage error");
        }

        LOGGER.info("sendTemplateMessage userOpenId={}, result={}", userOpenId, jsonResult);

    }
}
