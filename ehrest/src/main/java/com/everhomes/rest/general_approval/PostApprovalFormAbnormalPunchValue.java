package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>abnormalDate: 异常日期 YYYY-MM-DD</li>
 * <li>abnormalItem: 异常班次</li>
 * <li>abnormalReason: 异常事由</li>
 * <li>punchIntervalNo: 异常是第几个班次</li>
 * <li>punchType: 异常是上班/下班</li>
 * </ul>
 */
public class PostApprovalFormAbnormalPunchValue {

	private String abnormalDate;
	
	private String abnormalItem;

	private String abnormalReason;
	
	private Integer punchIntervalNo;
	
	private Byte punchType;
	

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

	public String getAbnormalReason() {
		return abnormalReason;
	}

	public void setAbnormalReason(String abnormalReason) {
		this.abnormalReason = abnormalReason;
	}

	public Integer getPunchIntervalNo() {
		return punchIntervalNo;
	}

	public void setPunchIntervalNo(Integer punchIntervalNo) {
		this.punchIntervalNo = punchIntervalNo;
	}

	public Byte getPunchType() {
		return punchType;
	}

	public void setPunchType(Byte punchType) {
		this.punchType = punchType;
	}

}
