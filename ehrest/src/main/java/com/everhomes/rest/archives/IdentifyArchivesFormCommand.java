package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>organizationId: 公司 id</li>
 * </ul>
 */
public class IdentifyArchivesFormCommand {

    private Long organizationId;

    public IdentifyArchivesFormCommand() {
    }

    public IdentifyArchivesFormCommand(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
