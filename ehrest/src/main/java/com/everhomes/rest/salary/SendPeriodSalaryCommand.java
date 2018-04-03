// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型:Organization</li>
 * <li>ownerId: 所属id</li>
 * <li>salaryPeriodGroupId: 某期薪酬批次id</li>
 * <li>sendTime: 发送时间的时间戳:如果为null则立即发送</li>
 * </ul>
 */
public class SendPeriodSalaryCommand {

	private String ownerType;

	private Long ownerId;

	private Long salaryPeriodGroupId;

	private Long sendTime;

	public SendPeriodSalaryCommand() {

	}

	public SendPeriodSalaryCommand(String ownerType, Long ownerId, Long salaryPeriodGroupId, Long sendTime) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.salaryPeriodGroupId = salaryPeriodGroupId;
		this.sendTime = sendTime;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getSalaryPeriodGroupId() {
		return salaryPeriodGroupId;
	}

	public void setSalaryPeriodGroupId(Long salaryPeriodGroupId) {
		this.salaryPeriodGroupId = salaryPeriodGroupId;
	}

	public Long getSendTime() {
		return sendTime;
	}

	public void setSendTime(Long sendTime) {
		this.sendTime = sendTime;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
