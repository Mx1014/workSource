package com.everhomes.pay.oauth2;

import com.everhomes.pay.PayConstants;
import com.everhomes.pay.oauth.AuthorizationCommand;
import com.everhomes.user.UserContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

@Controller
@RequestMapping("/pay/oauth2")
public class PayOauth2Controller extends PayOauth2BaseController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PayOauth2Controller.class);

    @Autowired
    private PayOauth2Service payOauth2Service;

    @RequestMapping("/authorize_logon")
    @ResponseBody
    public Object getToken(
            @RequestParam(value = "payUserId") Long payUserId,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String state = UserContext.getCurrentNamespaceId() + "_" + UserContext.currentUserId();
        AuthorizationCommand cmd = new AuthorizationCommand();
        cmd.setResponseType("code");
        cmd.setPayUserId(payUserId);
        cmd.setRedirectURI(URLEncoder.encode(configProvider.getValue(PayConstants.CORE_SERVER_URL, "") + PayConstants.OAUTH2_REDIRECT_LOGON_URL, "UTF-8"));
        cmd.setScope("basic");
        cmd.setState(state);

        return payOauth2Service.authorizeLogon(cmd);
    }

//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        try {
//            HttpPost post = new HttpPost(String.format("%s?response_type=code&app_key=%s&pay_user_id=%s&redirect_uri=%s&scope=basic&state=%s#oauth2_redirect",
//                    configProvider.getValue(PayConstants.PAY_AUTHORIZE_SERVICE_URL, ""),
//                    payMapping.getAppKey(),
//                    payUserId,
//                    URLEncoder.encode(configProvider.getValue(PayConstants.PAY_AUTHORIZE_LOGON_REDIRECT_URL, "") + "", "UTF-8"),
//                    state
//            ));
//
//            CloseableHttpResponse res = httpclient.execute(post);
//            try {
//                LOGGER.info(res.toString());
//                HttpEntity resEntity = res.getEntity();
//                LOGGER.info(resEntity.toString());
//                if (resEntity != null) {
//                    Gson gson = new Gson();
//                    String responseString = IOUtils.toString(resEntity.getContent(), "UTF-8");
//                    OAuth2AccessTokenResponse tokenResponse = gson.fromJson(responseString, OAuth2AccessTokenResponse.class);
//
////                    this.tokenManager.setAccessToken(state, tokenResponse.getAccess_token());
//                    String s_token=tokenResponse.getAccess_token();
//                    LOGGER.info("s_token: "+s_token);
//
//                    return new RestResponse(s_token);
//                }
//
//            } finally {
//                res.close();
//            }
////            return null;
//        } finally {
//            httpclient.close();
//        }
//
//        return "iresult";
//
//    }
}