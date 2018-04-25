package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>payslipId: id</li>
 * <li>salaryPeriod: 期数YYYYMM</li>
 * <li>name: 工资表名称</li>
 * <li>sendCount: 发放人数</li>
 * <li>viewCount: 查看人数</li>
 * <li>confirmCount: 确认人数</li>
 * <li>revokeCount: 撤回人数</li>
 * <li>operatorUid: 发放者id</li>
 * <li>operatorName: 发放人姓名</li>
 * <li>operateTime: 发放时间</li>
 * </ul>
 */
public class PayslipDTO {

	private Long payslipId;
    private String salaryPeriod;
    private String name;
    private Integer sendCount;
    private Integer viewCount;
    private Integer confirmCount;
    private Integer revokeCount;
    private Long operatorUid;
    private String operatorName;
    private Long operateTime;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getSalaryPeriod() {
		return salaryPeriod;
	}
	public void setSalaryPeriod(String salaryPeriod) {
		this.salaryPeriod = salaryPeriod;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSendCount() {
		return sendCount;
	}
	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}
	public Integer getViewCount() {
		return viewCount;
	}
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
	public Integer getConfirmCount() {
		return confirmCount;
	}
	public void setConfirmCount(Integer confirmCount) {
		this.confirmCount = confirmCount;
	}
	public Integer getRevokeCount() {
		return revokeCount;
	}
	public void setRevokeCount(Integer revokeCount) {
		this.revokeCount = revokeCount;
	}

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

	public Long getPayslipId() {
		return payslipId;
	}

	public void setPayslipId(Long payslipId) {
		this.payslipId = payslipId;
	} 
    
    
}
