// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>salaryGroupId: 薪酬批次id</li>
 * <li>userId: user id</li>
 * <li>SalaryEmployeeOriginVals: 个人薪酬设定参考{@link com.everhomes.rest.salary.SalaryEmployeeOriginValDTO}</li>
 * </ul>
 */
public class SaveSalaryEmployeeOriginValsCommand {

	private Long salaryGroupId;

	private Long userId;

	@ItemType(SalaryEmployeeOriginValDTO.class)
	private List<SalaryEmployeeOriginValDTO> SalaryEmployeeOriginVals;

	public SaveSalaryEmployeeOriginValsCommand() {

	}

	public SaveSalaryEmployeeOriginValsCommand(Long salaryGroupId, Long userId, List<SalaryEmployeeOriginValDTO> SalaryEmployeeOriginVals) {
		super();
		this.salaryGroupId = salaryGroupId;
		this.userId = userId;
		this.SalaryEmployeeOriginVals = SalaryEmployeeOriginVals;
	}

	public Long getSalaryGroupId() {
		return salaryGroupId;
	}

	public void setSalaryGroupId(Long salaryGroupId) {
		this.salaryGroupId = salaryGroupId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<SalaryEmployeeOriginValDTO> getSalaryEmployeeOriginVals() {
		return SalaryEmployeeOriginVals;
	}

	public void setSalaryEmployeeOriginVals(List<SalaryEmployeeOriginValDTO> SalaryEmployeeOriginVals) {
		this.SalaryEmployeeOriginVals = SalaryEmployeeOriginVals;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
