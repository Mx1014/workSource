package com.everhomes.statistics.transaction;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.business.BusinessDTO;
import com.everhomes.rest.statistics.transaction.ListStatServiceSettlementAmountsCommand;
import com.everhomes.rest.statistics.transaction.ListStatShopTransactionsResponse;
import com.everhomes.rest.statistics.transaction.ListStatTransactionCommand;
import com.everhomes.rest.statistics.transaction.StatServiceSettlementResultDTO;
import com.everhomes.rest.statistics.transaction.StatTaskLogDTO;

public interface StatTransactionService {

	List<StatTaskLogDTO> excuteSettlementTask(Long startDate, Long endDate);
	
	List<StatServiceSettlementResultDTO> listStatServiceSettlementAmounts(ListStatServiceSettlementAmountsCommand cmd);
	
	List<StatServiceSettlementResultDTO> listStatServiceSettlementAmountDetails(ListStatServiceSettlementAmountsCommand cmd);
	
	void exportStatServiceSettlementAmounts(ListStatServiceSettlementAmountsCommand cmd, HttpServletResponse response);
	
	ListStatShopTransactionsResponse listStatShopTransactions(ListStatTransactionCommand cmd);
	
	void exportStatShopTransactions(ListStatTransactionCommand cmd, HttpServletResponse response);
	
	List<BusinessDTO> listZuoLinBusinesses();
}
