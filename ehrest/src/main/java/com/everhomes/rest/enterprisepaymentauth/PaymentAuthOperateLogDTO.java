package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>operatorUid: 操作人uid</li>
 * <li>operatorName: 操作人姓名</li>
 * <li>operateTime: 操作时间</li>
 * <li>operateLog: 操作说明</li>
 * </ul>
 */
public class PaymentAuthOperateLogDTO {
	private Long operatorUid;
	private String operatorName;
	private Long operateTime;
	private String operateLog;

	public Long getOperatorUid() {
		return operatorUid;
	}

	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Long getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Long operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperateLog() {
		return operateLog;
	}

	public void setOperateLog(String operateLog) {
		this.operateLog = operateLog;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
