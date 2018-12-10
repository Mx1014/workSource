package com.everhomes.openapi.ruian;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.activity.ruian.WebApiRuianHelper;
import com.everhomes.rest.openapi.OpenApiRedirectHandler;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.util.Map;

/**
 * 对接瑞安猫酷
 */
@Component(OpenApiRedirectHandler.PREFIX + "ruian")
public class RuianOpenApiRedirectHandler implements OpenApiRedirectHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuianOpenApiRedirectHandler.class);
    private static final Integer RUIAN_NAMESPACE_ID = 999929;
    private static final String MALLCOO_URL = "https://openapi10.mallcoo.cn/User/MallCard/v2/Open/ByMallCardTypeID/";

    @Autowired
    private UserService userService;

    /**
     * 去猫酷拿ticket然后转跳
     */
    @Override
    public String build(String redirectUrl, Map<String, String[]> parameterMap) {
        try {
            String callBackUrl =  parameterMap.get("redirectUrl")[0];
            String ticket = "";

            Long userId = UserContext.currentUserId();
            Integer namespaceId = UserContext.getCurrentNamespaceId();

            //仅瑞安域空间允许用此接口
//            if ( namespaceId == null || RUIAN_NAMESPACE_ID.compareTo(namespaceId) != 0 ) {
//                LOGGER.error("user in namespace[" + namespaceId + "] is not allowed to redirect to ruian");
//                return null;
//            }

            ticket = getTicketFromMallcoo(userId);
            callBackUrl = URLEncoder.encode(callBackUrl, "utf-8");

            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("ticket",ticket);
            object.put("callbackurl1",callBackUrl);
            String jsonStr = object.toString();

            return UriComponentsBuilder.fromHttpUrl(redirectUrl)
                    .queryParam("ticket", ticket)
                    .queryParam("callbackurl1", callBackUrl)
                    .build()
                    .toUriString();
        } catch (Exception e) {
            LOGGER.error("RuianOpenApiRedirectHandler bulid failed, because " + e);
        }
        return null;
    }

    /**
     * 用手机号去猫酷拿ticket
     */
    private String getTicketFromMallcoo(long userId) {
        try {
            String identifierToken = userService.getUserIdentifier(userId).getIdentifierToken();

            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            if(identifierToken == null){
                LOGGER.error("error occured in getTicketFromMallcoo cause there is no identifier token(phoneNum) for user [" + userId + "]");
            }
            object.put("Mobile",identifierToken);
            String resultStr = WebApiRuianHelper.getIns().doPost(MALLCOO_URL, object.toString(),null);

            JSONObject resultJson = JSONObject.parseObject(resultStr);
            // 解析返回值中的ticket，返回Code值为1表示新注册成功，307表示已是会员
            if ( null != resultJson && ( (int)resultJson.get("Code") == 1 || (int)resultJson.get("Code") == 307 ) ) {
                String data = resultJson.getString("Data");
                JSONObject userInfo = JSONObject.parseObject(data);
                if (null != userInfo) {
                    return  userInfo.getString("Ticket");
                }
            }
            LOGGER.error("error occured in getTicketFromMallcoo cause wrong result returned from mallcoo, response json = " + resultStr);
        } catch (Exception e){
            LOGGER.error("error occured in getTicketFromMallcoo cause " + e);
        }
        return null;
    }
}
