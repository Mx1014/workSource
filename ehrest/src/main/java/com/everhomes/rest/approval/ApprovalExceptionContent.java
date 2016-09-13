// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>punchDate: 打卡日期</li>
 * <li>exceptionRequestType: 异常类型，{@link com.everhomes.rest.approval.ExceptionRequestType}</li>
 * <li>punchDetail: 打卡详情</li>
 * <li>punchStatusName: 打卡状态名称</li>
 * </ul>
 */
public class ApprovalExceptionContent {
	private Long punchDate;
	private Byte exceptionRequestType;
	private String punchDetail;
	private String punchStatusName;

	public Long getPunchDate() {
		return punchDate;
	}

	public void setPunchDate(Long punchDate) {
		this.punchDate = punchDate;
	}

	public Byte getExceptionRequestType() {
		return exceptionRequestType;
	}

	public void setExceptionRequestType(Byte exceptionRequestType) {
		this.exceptionRequestType = exceptionRequestType;
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
}
