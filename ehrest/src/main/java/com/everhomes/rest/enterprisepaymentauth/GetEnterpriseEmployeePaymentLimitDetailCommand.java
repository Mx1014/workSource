// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>detailId: 员工detailId</li>
 * </ul>
 */
public class GetEnterpriseEmployeePaymentLimitDetailCommand {
	private Long organizationId;
	private Long detailId;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
