package com.everhomes.openapi.zhenzhihui;

import com.everhomes.payment.taotaogu.AESCoder;
import com.everhomes.rest.openapi.OpenApiRedirectHandler;
import com.everhomes.user.UserContext;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;

/**
 * 对接圳智慧
 * 生成ticket并redirect
 * @author chenhe
 */
@Component(OpenApiRedirectHandler.PREFIX + "zhyq")
public class ZHYQOpenRpiRedirectHandler implements OpenApiRedirectHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZHYQOpenRpiRedirectHandler.class);
    private static final Integer NAMESPACEALLOWED = 2; //仅XXX域空间允许跳第三方

    @Autowired
    private TicketProvider ticketProvider;

    @Override
    public String build(String redirectUrl, Map<String, String[]> parameterMap) {
        try {
            Long userId = UserContext.currentUserId();
            Integer namespaceId = UserContext.getCurrentNamespaceId();

            //仅XXX域空间被允许跳第三方
            if ( namespaceId == null || NAMESPACEALLOWED.compareTo(namespaceId) != 0 ) {
                LOGGER.error("user in namespace[" + namespaceId + "] is not allowed to redirect to zhenzhihui");
            }

            //生成ticket并保存
            String ticket = ticketProvider.createTicket4User(userId);

            //AES加密ticket
            String encryptedTicket = Base64.encodeBase64String(AESCoder.encrypt(ticket.getBytes("utf-8"), ZHYQOpenController.KEY.getBytes("utf-8")));

            return UriComponentsBuilder.fromHttpUrl(redirectUrl)
                    .queryParam("TICKET", encryptedTicket)
                    .build()
                    .toUriString();
        } catch (Exception e) {
            LOGGER.error("ZHYQOpenRpiRedirectHandler bulid is failed, because " + e);
        }
        return null;
    }
}
