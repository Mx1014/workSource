package com.everhomes.openapi.zhenzhihui;

import com.everhomes.payment.taotaogu.AESCoder;
import com.everhomes.rest.openapi.OpenApiRedirectHandler;
import com.everhomes.user.UserContext;
import com.everhomes.zhenzhihui.ZhenZhiHuiServiceImpl;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 对接圳智慧
 */
@Component(OpenApiRedirectHandler.PREFIX + "zhenzhihui")
public class ZhenzhihuiOpenApiRedirectHandler implements OpenApiRedirectHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZhenzhihuiOpenApiRedirectHandler.class);

    @Autowired
    private TicketProvider ticketProvider;

    /**
     * 生成ticket并redirect
     */
    @Override
    public String build(String redirectUrl, Map<String, String[]> parameterMap) {
        try {
            String redirectCode = parameterMap.get("redirectCode")[0];

            Long userId = UserContext.currentUserId();
            Integer namespaceId = UserContext.getCurrentNamespaceId();

            //仅圳智慧域空间允许用此接口
            if ( namespaceId == null || ZhenZhiHuiServiceImpl.ZHENZHIHUI_NAMESPACE_ID.compareTo(namespaceId) != 0 ) {
                LOGGER.error("user in namespace[" + namespaceId + "] is not allowed to redirect to zhenzhihui");
                return null;
            }

            //生成ticket并保存
            String ticket = ticketProvider.createTicket4User(userId, redirectCode);

            //AES加密ticket
            String encryptedTicket = Base64.encodeBase64String(AESCoder.encrypt(ticket.getBytes("utf-8"), ZhenzhihuiOpenController.KEY.getBytes("utf-8")));
            String urlTicket = URLEncoder.encode(encryptedTicket, "utf-8");

            return UriComponentsBuilder.fromHttpUrl(redirectUrl)
                    .queryParam("TICKET", urlTicket)
                    .build()
                    .toUriString();
        } catch (Exception e) {
            LOGGER.error("ZhenzhihuiOpenApiRedirectHandler bulid is failed, because " + e);
        }
        return null;
    }
}
