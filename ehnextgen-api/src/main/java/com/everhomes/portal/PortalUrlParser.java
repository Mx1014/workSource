package com.everhomes.portal;

public interface PortalUrlParser {
    Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel);
}
