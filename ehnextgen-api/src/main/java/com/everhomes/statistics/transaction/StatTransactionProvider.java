package com.everhomes.statistics.transaction;

import java.util.List;

import org.jooq.Condition;

import com.everhomes.listing.CrossShardListingLocator;


public interface StatTransactionProvider {
	
	void createStatOrder(StatOrder statOrder);
	
	void deleteStatOrderByDate(String date, String resourceType);
	
	StatOrder findStatOrderByOrderNoAndResourceType(String orderNo, String resourceType);

	void createStatTransaction(StatTransaction statTransaction);
	
	void deleteStatTransactionByDate(String date, String resourceType);
	/**
	 * 按天统计交易流水的结算数据
	 * @param date
	 * @return
	 */
	List<StatSettlement>  countStatTransactionSettlement(String date);
	
	void createStatRefund(StatRefund statRefund);
	
	void deleteStatRefundByDate(String date, String resourceType);
	
	/**
	 * 按天统计退款流水的结算数据
	 * @param date
	 * @return
	 */
	List<StatSettlement>  countStatRefundSettlement(String date);
	
	/**
	 * 按日期查询统计的结算数据
	 * @param cond
	 * @param date
	 * @return
	 */
	StatSettlement findStatSettlementByDate(Condition cond, String date);
	
	void createStatSettlement(StatSettlement statSettlement);
	
	void deleteStatSettlementByDate(String date);
	
	void updateStatSettlement(StatSettlement statSettlement);
	
	/**
	 * 按时间支付渠道统计结算结果数据
	 * @param paidChannel
	 * @param date
	 * @return
	 */
	List<StatServiceSettlementResult>  countStatServiceSettlementResult(Byte paidChannel, String date);
	
	void createStatServiceSettlementResult(StatServiceSettlementResult statServiceSettlementResult);
	
	void deleteStatServiceSettlementResultByDate(String date);
	
	void updateStatServiceSettlementResult(StatServiceSettlementResult statServiceSettlementResult);
	
	StatServiceSettlementResult findStatServiceSettlementResultDate(Condition cond, String date);
	
	List<StatServiceSettlementResult> listStatServiceSettlementResult(Condition cond, String startDate, String endDate);
	
	/**
	 * 按service 时间统计
	 * @param cond
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<StatServiceSettlementResult> listCountStatServiceSettlementResultByService(Condition cond, String startDate, String endDate);
	
	void createStatTaskLog(StatTaskLog statTaskLog);
	
	void updateStatTaskLog(StatTaskLog statTaskLog);
	
	StatTaskLog findStatTaskLog(String date);
	
	StatServiceSettlementResult getStatServiceSettlementResultTotal(Condition cond, String startDate, String endDate);
	
	List<StatService> listStatServices(Integer namespaceId, String ownerType, Long ownerId);
	
	List<StatService> listStatServiceGroupByServiceTypes();
	
	List<StatTransaction> listStatTransactions(CrossShardListingLocator locator, Integer pageSize, String startDate, String endDate, String resourceId, String resourceType, Integer namespaceId, Long communityId, String serviceType);
	
	
	List<StatRefund> listStatRefunds(CrossShardListingLocator locator, Integer pageSize, String startDate,String endDate, String resourceId, String resourceType, Integer namespaceId, Long communityId, String serviceType);

	List<StatTransaction> listBusinessByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor,
			int pageSize);

	List<StatTransaction> listBusinessByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);
}
