package com.everhomes.openapi.shenzhihui;

import com.everhomes.oauth2.AuthorizationCode;
import com.everhomes.oauth2.OAuth2Provider;
import com.everhomes.payment.taotaogu.AESCoder;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.openapi.OpenApiRedirectHandler;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IdToken;
import com.everhomes.util.WebTokenGenerator;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.util.Map;

@Component(OpenApiRedirectHandler.PREFIX + "zhyq")
public class ZHYQOpenRpiRedirectHandler implements OpenApiRedirectHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZHYQOpenRpiRedirectHandler.class);
    private static final Integer NAMESPACEALLOWED = 2; //仅XXX域空间允许跳第三方
    private static final String KEY = "";

    @Autowired
    private OAuth2Provider oAuth2Provider;

    @Override
    public String build(String redirectUrl, Map<String, String[]> parameterMap) {
        try {
            Long id = UserContext.currentUserId();
            Integer namespaceId = UserContext.getCurrentNamespaceId();

            //仅XXX域空间被允许跳第三方
            if ( namespaceId == null || NAMESPACEALLOWED.compareTo(namespaceId) != 0 ) {
                LOGGER.error("user in namespace[" + namespaceId + "] is not allowed to redirect to zhenzhihui");
            }

            String token = WebTokenGenerator.getInstance().toWebToken(new IdToken(id));
            String encryptedToken = Base64.encodeBase64String(AESCoder.encrypt(Base64.decodeBase64(token), Base64.decodeBase64(KEY)));













//            Long id = 505387l;

            AuthorizationCode code = new AuthorizationCode();
//            code.setScope(cmd.getScope());
            code.setRedirectUri(redirectUrl);
            code.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            code.setModifyTime(code.getCreateTime());
            code.setExpirationTime(new Timestamp(DateHelper.currentGMTTime().getTime() + 24 * 60 * 60 * 1000));
            UserContext u = UserContext.current();
            AppContext p = u.getAppContext();
            Long appid = p.getAppId();
            code.setAppId(UserContext.current().getAppContext().getAppId());
            code.setGrantorUid(id);
            oAuth2Provider.createAuthorizationCode(code);

            return UriComponentsBuilder.fromHttpUrl(redirectUrl)
                    .queryParam("TICKET", encryptedToken)
                    .build()
                    .toUriString();
        } catch (Exception e) {
            LOGGER.error("ZHYQOpenRpiRedirectHandler bulid is failed, because " + e);
        }
        return null;
    }
}
