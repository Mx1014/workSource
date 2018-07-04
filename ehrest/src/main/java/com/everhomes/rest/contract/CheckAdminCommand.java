package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>organizationId: 企业id</li>
 * </ul>
 * Created by ying.xiong on 2018/1/3.
 */
public class CheckAdminCommand {
    private Integer namespaceId;

    private Long organizationId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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
