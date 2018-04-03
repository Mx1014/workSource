// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>abnormalEmployeeNumber: 异常员工数量</li>
 * </ul>
 */
public class GetAbnormalEmployeeNumberResponse {

	private Integer abnormalEmployeeNumber;

	public GetAbnormalEmployeeNumberResponse() {

	}

	public GetAbnormalEmployeeNumberResponse(Integer abnormalStaffNumber) {
		super();
		this.abnormalEmployeeNumber = abnormalStaffNumber;
	}

	public Integer getAbnormalStaffNumber() {
		return abnormalEmployeeNumber;
	}

	public void setAbnormalStaffNumber(Integer abnormalStaffNumber) {
		this.abnormalEmployeeNumber = abnormalStaffNumber;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
