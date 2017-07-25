// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型:Organization</li>
 * <li>ownerId: 所属id</li>
 * <li>period: 查哪一期薪酬批次:现在都是按月发放所以传YYYYMM如201702</li>
 * <li>status: 薪酬组本期状态:核算状态页面传空或者{0,1,3,4},发放状态页面传{1,3,4}</li>
 * </ul>
 */
public class ListPeriodSalaryCommand {

	private String ownerType;

	private Long ownerId;

	private String period;

	@ItemType(Byte.class)
	private List<Byte> status;

	public ListPeriodSalaryCommand() {

	}

	public ListPeriodSalaryCommand(String ownerType, Long ownerId, String period, List<Byte> status) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.period = period;
		this.status = status;
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

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public List<Byte> getStatus() {
		return status;
	}

	public void setStatus(List<Byte> status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
