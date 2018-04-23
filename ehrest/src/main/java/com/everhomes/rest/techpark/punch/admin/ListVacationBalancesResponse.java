// @formatter:off
package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>vacationBalances: 假期余额列表 参考{@link com.everhomes.rest.techpark.punch.admin.VacationBalanceDTO}</li>
 * <li>nextPageOffset: 下页页码</li>
 * </ul>
 */
public class ListVacationBalancesResponse {

	@ItemType(VacationBalanceDTO.class)
	private List<VacationBalanceDTO> vacationBalances;

	private Integer nextPageOffset;
	public ListVacationBalancesResponse() {

	}

	public ListVacationBalancesResponse(List<VacationBalanceDTO> vacationBalances) {
		super();
		this.vacationBalances = vacationBalances;
	}

	public List<VacationBalanceDTO> getVacationBalances() {
		return vacationBalances;
	}

	public void setVacationBalances(List<VacationBalanceDTO> vacationBalances) {
		this.vacationBalances = vacationBalances;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}
}
