// @formatter:off
package com.everhomes.techpark.punch.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PunchServiceImpl implements PunchService {

	@Override
	public ListVacationBalancesResponse listVacationBalances(ListVacationBalancesCommand cmd) {
	
		return new ListVacationBalancesResponse();
	}

	@Override
	public void updateVacationBalances(UpdateVacationBalancesCommand cmd) {
	

	}

	@Override
	public void batchUpdateVacationBalances(BatchUpdateVacationBalancesCommand cmd) {
	

	}

	@Override
	public ListVacationBalanceLogsResponse listVacationBalanceLogs(ListVacationBalanceLogsCommand cmd) {
	
		return new ListVacationBalanceLogsResponse();
	}

	@Override
	public void exportVacationBalances(ExportVacationBalancesCommand cmd) {
	

	}

	@Override
	public void importVacationBalances(ImportVacationBalancesCommand cmd) {
	

	}

}