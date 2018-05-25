package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>requestId</li>
 * <li>organizationId:管理公司id</li>
 * </ul>
 */
public class RequestIdCommand {

    private Long requestId;
    private Long organizationId;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
