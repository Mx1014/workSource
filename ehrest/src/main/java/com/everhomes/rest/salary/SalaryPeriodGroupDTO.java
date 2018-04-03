package com.everhomes.rest.salary;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>id: id</li>
 * <li>salaryPeriod: 期数</li>
 * <li>organizationGroupId: 薪酬批次id</li>
 * <li>groupName: 薪酬批次名称</li>
 * <li>sendTime: 发送时间</li>
 * <li>status:本期批次状态 参考{@link com.everhomes.rest.salary.SalaryGroupStatus} </li>
 * </ul>
 */
public class SalaryPeriodGroupDTO {
	private Long id; 
    private String salaryPeriod;
    private Long organizationGroupId;
    private String groupName; 
    private Timestamp sendTime;
    private Byte status;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSalaryPeriod() {
		return salaryPeriod;
	}
	public void setSalaryPeriod(String salaryPeriod) {
		this.salaryPeriod = salaryPeriod;
	}
	public Long getOrganizationGroupId() {
		return organizationGroupId;
	}
	public void setOrganizationGroupId(Long organizationGroupId) {
		this.organizationGroupId = organizationGroupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
}
