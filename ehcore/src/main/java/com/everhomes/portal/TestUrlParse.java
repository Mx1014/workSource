package com.everhomes.portal;

import org.springframework.stereotype.Component;

@Component
public class TestUrlParse implements PortalUrlParser{
    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {
        return 1111L;
    }
}
