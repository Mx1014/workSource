//@formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2018/6/13.
 */
/**
 *<ul>
 * <li>reservationId:预约id</li>
 * <li>namespaceId: 域空间id，用于权限校验</li>
 * <li>organizationId: 管理公司id，用于权限校验</li>
 * <li>communityId: 园区id，用于权限校验</li>
 *</ul>
 */
public class CancelReservationCommand {
    private Long reservationId;
    private Long communityId;
    private Integer namespaceId;
    private Long organizationId;

    public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }


    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
    
    @Override
    public String toString() {
    	return StringHelper.toJsonString(this);
    }
}
