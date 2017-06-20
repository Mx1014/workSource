// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>abnormalStaffNumber: 异常员工数量</li>
 * </ul>
 */
public class GetAbnormalStaffNumberResponse {

	private Integer abnormalStaffNumber;

	public GetAbnormalStaffNumberResponse() {

	}

	public GetAbnormalStaffNumberResponse(Integer abnormalStaffNumber) {
		super();
		this.abnormalStaffNumber = abnormalStaffNumber;
	}

	public Integer getAbnormalStaffNumber() {
		return abnormalStaffNumber;
	}

	public void setAbnormalStaffNumber(Integer abnormalStaffNumber) {
		this.abnormalStaffNumber = abnormalStaffNumber;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
