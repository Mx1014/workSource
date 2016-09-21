// @formatter:off
package com.everhomes.rest.approval;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>requestId: 申请ID</li>
 * <li>punchDate: 打卡日期</li>
 * <li>exceptionRequestType: 异常申请类型，参考{@link com.everhomes.rest.approval.ExceptionRequestType}</li>
 * <li>nickName: 姓名</li>
 * <li>reason: 申请理由</li>
 * <li>punchStatusName: 打卡状态</li>
 * </ul>
 */
public class ExceptionRequestDTO {
	private Long requestId;
	private Timestamp punchDate;
	private Byte exceptionRequestType;
	private String nickName;
	private String reason;
	private String punchStatusName;

	public Byte getExceptionRequestType() {
		return exceptionRequestType;
	}

	public void setExceptionRequestType(Byte exceptionRequestType) {
		this.exceptionRequestType = exceptionRequestType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Timestamp getPunchDate() {
		return punchDate;
	}

	public void setPunchDate(Timestamp punchDate) {
		this.punchDate = punchDate;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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
