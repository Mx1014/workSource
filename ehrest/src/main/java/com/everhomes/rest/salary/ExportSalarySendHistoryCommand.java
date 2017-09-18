// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型:Organization</li>
 * <li>ownerId: 所属id</li>
 * <li>keyWord: 关键字,目前查询员工姓名</li>
 * <li>organizationId: 查询组织架构部门id</li>
 * <li>beginTime: 查询开始时间</li>
 * <li>endTime: 查询结束时间</li>
 * </ul>
 */
public class ExportSalarySendHistoryCommand {

	private String ownerType;

	private Long ownerId;

	private String keyWord;

	private Long organizationId;

	private Long beginTime;

	private Long endTime;

	public ExportSalarySendHistoryCommand() {

	}

	public ExportSalarySendHistoryCommand(String ownerType, Long ownerId, String keyWord, Long organizationId, Long beginTime, Long endTime) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.keyWord = keyWord;
		this.organizationId = organizationId;
		this.beginTime = beginTime;
		this.endTime = endTime;
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

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
