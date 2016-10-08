// @formatter:off
package com.everhomes.approval;

import com.everhomes.server.schema.tables.pojos.EhApprovalDayActualTime;
import com.everhomes.util.StringHelper;

public class ApprovalDayActualTime extends EhApprovalDayActualTime {
	
	private static final long serialVersionUID = -3646879692640094398L;

	private Long categoryId;
	
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}