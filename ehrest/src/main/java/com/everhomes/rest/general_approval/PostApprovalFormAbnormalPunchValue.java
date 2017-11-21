package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>abnormalDate: 异常日期</li>
 * <li>abnormalItem: 异常班次</li>
 * </ul>
 */
public class PostApprovalFormAbnormalPunchValue {

	private String abnormalDate;

	private String abnormalItem;

	public String getAbnormalDate() {
		return abnormalDate;
	}

	public void setAbnormalDate(String abnormalDate) {
		this.abnormalDate = abnormalDate;
	}

	public String getAbnormalItem() {
		return abnormalItem;
	}

	public void setAbnormalItem(String abnormalItem) {
		this.abnormalItem = abnormalItem;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
