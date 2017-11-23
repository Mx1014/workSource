package com.everhomes.rest.common;

/**
 * <ul>
 * <li>portalType：当前门户类型，参考{@link com.everhomes.rest.common.PortalType}</li>
 * <li>portalId：当前门户id，类型zuolin时 为0</li>
 * </ul>
 */
public class PortalCommand {

    private Long portalId;

    private String portalType;

    public Long getPortalId() {
        return portalId;
    }

    public void setPortalId(Long portalId) {
        this.portalId = portalId;
    }

    public String getPortalType() {
        return portalType;
    }

    public void setPortalType(String portalType) {
        this.portalType = portalType;
    }
}
