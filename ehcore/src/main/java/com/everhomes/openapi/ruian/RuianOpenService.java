package com.everhomes.openapi.ruian;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.activity.ruian.WebApiRuianHelper;
import com.everhomes.rest.openapi.ruian.MallcooAutoLoginCommand;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@Component
public class RuianOpenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuianOpenService.class);

    // 我们的communityId(key)和mallcooId(value)的映射表
    public static final Map<String, String> PROJECT_MAPPING = new HashMap(){{put("240111044332063582","10776");put("default","10764");}};

    // 去mallcoo获取ticket的url
    public static final String TICKET_GET_RUL = "https://openapi10.mallcoo.cn/User/MallCard/v2/Open/ByMallCardTypeID/";
    // 重定向到自动登录的地址,使用时注意把MALLID_PLACEHOLDER替换为mallId以适用猫酷的不同商城
    public static final String REDIRECT_TO_LOGIN_URL = "https://m.mallcoo.cn/a/user/MALLID_PLACEHOLDER/identity/verifytoken";
    // 回调地址，使用时注意把MALLID_PLACEHOLDER替换为mallId以适用猫酷的不同商城
    public static final String CALLBACK_URL = "https://m.mallcoo.cn/a/home/MALLID_PLACEHOLDER";
    public static final String MALLID_PLACEHOLDER = "MALLID_PLACEHOLDER";
    public static final Integer RUIAN_NAMESPACE_ID = 999929;

    @Autowired
    private UserService userService;

    /**
     * 去猫酷拿ticket然后转跳
     */
    public HttpHeaders buildRedirectHeader(MallcooAutoLoginCommand mallcooAutoLoginCommand) {

        String communityId = mallcooAutoLoginCommand.getCommunityId() == null ? "default" : mallcooAutoLoginCommand.getCommunityId();
        String callbackUrl = mallcooAutoLoginCommand.getCallbackUrl() == null ? CALLBACK_URL : mallcooAutoLoginCommand.getCallbackUrl();
        String mallId = PROJECT_MAPPING.get(communityId) == null ? PROJECT_MAPPING.get("default") : PROJECT_MAPPING.get(communityId);

        String redirectToLoginUrl = REDIRECT_TO_LOGIN_URL.replaceAll(MALLID_PLACEHOLDER, mallId);
        callbackUrl = callbackUrl.replaceAll(MALLID_PLACEHOLDER, mallId);
        String ticketGetUrl = TICKET_GET_RUL.replaceAll(MALLID_PLACEHOLDER, mallId);

        Long userId = UserContext.currentUserId();
        // 游客用户直接重定向到要转跳的地址不进行自动登录
        if ( userId == null || userId.compareTo(0L) == 0){
            LOGGER.info("unlogined user redirect without auto login");
            return buildHeaders(callbackUrl);
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        //仅瑞安域空间允许用此接口
        if ( namespaceId == null || RUIAN_NAMESPACE_ID.compareTo(namespaceId) != 0 ) {
            LOGGER.error("user in namespace[" + namespaceId + "] is not allowed to redirect to ruian");
            return null;
        }

        String identifierToken = userService.getUserIdentifier(userId).getIdentifierToken();
        if(identifierToken == null){
            LOGGER.warn("error occured in getTicketFromMallcoo cause there is no identifier token(phoneNum) for user [" + userId + "]");
            return buildHeaders(callbackUrl);
        }

        String ticket = getTicketFromMallcoo(identifierToken, ticketGetUrl);
        try {
            callbackUrl = URLEncoder.encode(callbackUrl, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            LOGGER.error("encode string: \"" + callbackUrl + "\" to utf-8 failed !");
            e1.printStackTrace();
        }

        String uriStr = UriComponentsBuilder.fromHttpUrl(redirectToLoginUrl)
                .queryParam("ticket", ticket)
                .queryParam("callbackurl1", callbackUrl)
                .build()
                .toUriString();

        return buildHeaders(uriStr);
    }

    private HttpHeaders buildHeaders(String uriStr){
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setLocation(new URI(uriStr));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return headers;
    }

    /**
     * 用手机号去猫酷拿ticket
     */
    private String getTicketFromMallcoo(String phoneNum, String ticketGetUrl) {
        try {
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("Mobile",phoneNum);
            String resultStr = WebApiRuianHelper.getIns().doPost(ticketGetUrl, object.toString(),null);

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
