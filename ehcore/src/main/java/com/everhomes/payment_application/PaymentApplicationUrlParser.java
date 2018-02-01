package com.everhomes.payment_application;

import com.everhomes.portal.PortalUrlParser;
import org.springframework.stereotype.Component;

/**
 * Created by ying.xiong on 2018/1/25.
 */
@Component
public class PaymentApplicationUrlParser implements PortalUrlParser {
    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {
        Long res = null;
        if(actionType == 13 && "paymentApplication".equals(actionData)){
            res = 21300l;
        }
        return res;
    }
}
