// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>punchDate: 打卡日期</li>
 * <li>punchIntervalNo: 当天第几个班次</li>
 * <li>punchDetail: 打卡详情</li>
 * <li>punchStatusName: 打卡状态名称</li>
 * </ul>
 */
public class ApprovalExceptionContent {
	private Long punchDate;
	private Integer punchIntervalNo;
	private String punchDetail;
	private String punchStatusName;

	public Long getPunchDate() {
		return punchDate;
	}

	public void setPunchDate(Long punchDate) {
		this.punchDate = punchDate;
	}

	public String getPunchDetail() {
		return punchDetail;
	}

	public void setPunchDetail(String punchDetail) {
		this.punchDetail = punchDetail;
	}

	public String getPunchStatusName() {
		return punchStatusName;
	}

	public void setPunchStatusName(String punchStatusName) {
		this.punchStatusName = punchStatusName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Integer getPunchIntervalNo() {
		return punchIntervalNo;
	}

	public void setPunchIntervalNo(Integer punchIntervalNo) {
		this.punchIntervalNo = punchIntervalNo;
	} 
}
