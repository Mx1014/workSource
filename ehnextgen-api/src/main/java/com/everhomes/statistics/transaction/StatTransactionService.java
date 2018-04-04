package com.everhomes.statistics.transaction;

import com.everhomes.rest.business.BusinessDTO;
import com.everhomes.rest.statistics.transaction.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface StatTransactionService {

	List<StatTaskLogDTO> excuteSettlementTask(Long startDate, Long endDate);
	
	List<StatServiceSettlementResultDTO> listStatServiceSettlementAmounts(ListStatServiceSettlementAmountsCommand cmd);
	
	List<StatServiceSettlementResultDTO> listStatServiceSettlementAmountDetails(ListStatServiceSettlementAmountsCommand cmd);
	
	void exportStatServiceSettlementAmounts(ListStatServiceSettlementAmountsCommand cmd, HttpServletResponse response);
	
	ListStatShopTransactionsResponse listStatShopTransactions(ListStatTransactionCommand cmd);
	
	void exportStatShopTransactions(ListStatTransactionCommand cmd, HttpServletResponse response);
	
	List<BusinessDTO> listZuoLinBusinesses();

    void syncShopToStatOrderByDate(String date) throws Exception;

    void syncPaidPlatformToStatTransaction(String date) throws Exception;

    void syncNewPaidPlatformToStatTransaction(String date) throws Exception;
}
