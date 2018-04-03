package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>meterId: 表记id</li>
 *     <li>namespaceId: 域空间</li>
 * </ul>
 */
public class GetEnergyMeterCommand {

    @NotNull private Long organizationId;
    @NotNull private Long meterId;
    private Integer namespaceId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }
    
    public Integer getNamespaceId() {
		return namespaceId;
	}

    public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
