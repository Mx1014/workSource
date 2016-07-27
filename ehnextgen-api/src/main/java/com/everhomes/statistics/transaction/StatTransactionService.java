package com.everhomes.statistics.transaction;

import java.util.List;

import com.everhomes.rest.statistics.transaction.ListStatServiceSettlementAmountsCommand;

public interface StatTransactionService {

	List<StatTaskLog> excuteSettlementTask(Long startDate, Long endDate);
	
	List<StatServiceSettlementResult> listStatServiceSettlementAmounts(ListStatServiceSettlementAmountsCommand cmd);
	
	List<StatServiceSettlementResult> listStatServiceSettlementAmountDetails(ListStatServiceSettlementAmountsCommand cmd);
	
	void exportStatServiceSettlementAmounts(ListStatServiceSettlementAmountsCommand cmd);
}
