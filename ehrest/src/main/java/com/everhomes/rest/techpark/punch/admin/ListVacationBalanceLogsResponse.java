// @formatter:off
package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>vacationBalanceLogs: 假期余额修改记录列表</li>
 * <li>nextPageAnchor: nextPageAnchor</li>
 * </ul>
 */
public class ListVacationBalanceLogsResponse {

	@ItemType(VacationBalanceLogDTO.class)
	private List<VacationBalanceLogDTO> vacationBalanceLogs;

	private Long nextPageAnchor;
	public ListVacationBalanceLogsResponse() {

	}

	public ListVacationBalanceLogsResponse(List<VacationBalanceLogDTO> vacationBalanceLogs) {
		super();
		this.vacationBalanceLogs = vacationBalanceLogs;
	}

	public List<VacationBalanceLogDTO> getVacationBalanceLogs() {
		return vacationBalanceLogs;
	}

	public void setVacationBalanceLogs(List<VacationBalanceLogDTO> vacationBalanceLogs) {
		this.vacationBalanceLogs = vacationBalanceLogs;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

}
