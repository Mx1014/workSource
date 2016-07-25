package com.everhomes.statistics.transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jooq.Condition;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.pmsy.PmsyOrder;
import com.everhomes.organization.pmsy.PmsyProvider;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.payment.PaymentCardProvider;
import com.everhomes.payment.PaymentCardRechargeOrder;
import com.everhomes.payment.PaymentCardTransaction;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import com.everhomes.rest.payment.CardOrderStatus;
import com.everhomes.rest.payment.CardTransactionStatus;
import com.everhomes.rest.pmsy.PmsyOrderStatus;
import com.everhomes.rest.pmsy.PmsyOwnerType;
import com.everhomes.rest.statistics.transaction.PaidChannel;
import com.everhomes.rest.statistics.transaction.SettlementErrorCode;
import com.everhomes.rest.statistics.transaction.SettlementOrderType;
import com.everhomes.rest.statistics.transaction.SettlementResourceType;
import com.everhomes.rest.statistics.transaction.SettlementServiceType;
import com.everhomes.rest.statistics.transaction.SettlementStatOrderStatus;
import com.everhomes.rest.statistics.transaction.SettlementStatTransactionPaidStatus;
import com.everhomes.rest.statistics.transaction.StatTaskLock;
import com.everhomes.rest.statistics.transaction.StatTaskStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

public class StatTransactionServiceImpl implements StatTransactionService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StatTransactionServiceImpl.class);
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private StatTransactionProvider statTransactionProvider;
	
	@Autowired
	private PmsyProvider pmsyProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private ParkingProvider parkingProvider;
	
	@Autowired
	private PaymentCardProvider paymentCardProvider;
	
	@Autowired
	private UserProvider userProvider;
	
	
	@Override
	public StatTaskLog excuteSettlementTask(Long startDate, Long endDate) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		if(null == startDate || null == endDate || startDate > endDate){
			startDate = calendar.getTimeInMillis();
			endDate = calendar.getTimeInMillis();
		}
		
		//如果结束时间大于昨天，结束时间就取昨天的值
		if(DateUtil.dateToStr(new Date(endDate), DateUtil.YMR_SLASH).compareTo(DateUtil.dateToStr(calendar.getTime(), DateUtil.YMR_SLASH)) > 0){
			endDate = calendar.getTimeInMillis();
		}
		
		//获取范围内的所以日期
		List<Date> dDates = DateUtil.getStartToEndDates(new Date(startDate), new Date(endDate));
		
		for (Date date : dDates) {
			String sDate = DateUtil.dateToStr(date, DateUtil.YMR_SLASH);
			//按日期结算数据
			this.settlementByDate(sDate);
		}
		
		return null;
	}
	
	
	/**
	 * 按天生成结算
	 */
	private StatTaskLog settlementByDate(String date){
		
		LOGGER.debug("start production settlement data. date = {}", date);
		
		StatTaskLog statTaskLog = statTransactionProvider.findStatTaskLog(date);
		
		if(null == statTaskLog){
			statTaskLog = new StatTaskLog();
			statTaskLog.setIslock(StatTaskLock.LOCK.getCode());
			statTaskLog.setStatus(StatTaskStatus.SYNC_SHOP_ORDER.getCode());
			statTaskLog.setTaskNo(date);
			statTransactionProvider.createStatTaskLog(statTaskLog);
		}else{
			if(StatTaskLock.fromCode(statTaskLog.getIslock()) == StatTaskLock.LOCK){
				LOGGER.debug("settlement data task being executed, date = {}, lock = {} ", date, statTaskLog.getIslock());
				return statTaskLog;
			}
			//把生成结算数据的任务锁住，不让其他线程执行
			statTaskLog.setIslock(StatTaskLock.LOCK.getCode());
			statTransactionProvider.updateStatTaskLog(statTaskLog);
		}
		
		try {
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_SHOP_ORDER){
				this.syncShopToStatOrderByDate(date);
				statTaskLog.setStatus(StatTaskStatus.SYNC_PMSY_ORDER.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_PMSY_ORDER){
				this.syncPmsyToStatOrderByDate(date);
				statTaskLog.setStatus(StatTaskStatus.SYNC_PARKING_RECHARGE_ORDER.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_PARKING_RECHARGE_ORDER){
				this.syncParkingRechargeToStatOrderByDate(date);
				statTaskLog.setStatus(StatTaskStatus.SYNC_RENTAL_SITE_ORDER.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_RENTAL_SITE_ORDER){
				this.syncRentalSiteToStatOrderByDate(date);
				statTaskLog.setStatus(StatTaskStatus.SYNC_PAYMENT_CARD_ORDER.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}			
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_PAYMENT_CARD_ORDER){
				this.syncPaymentToStatOrderByDate(date);
				statTaskLog.setStatus(StatTaskStatus.SYNC_PAID_PLATFORM_TRANSACTION.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_PAID_PLATFORM_TRANSACTION){
				this.syncPaidPlatformToStatTransaction(date);
				statTaskLog.setStatus(StatTaskStatus.SYNC_PAYMENT_CARD_TRANSACTION.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_PAYMENT_CARD_TRANSACTION){
				this.syncPaymentToStatTransaction(date);
				statTaskLog.setStatus(StatTaskStatus.SYNC_PAID_PLATFORM_REFUND.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_PAID_PLATFORM_REFUND){
				this.syncPaidPlatformToStatRefund(date);
				statTaskLog.setStatus(StatTaskStatus.SYNC_PAYMENT_REFUND.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_PAYMENT_REFUND){
				this.syncPaymentToStatRefund(date);
				statTaskLog.setStatus(StatTaskStatus.GENERATE_SETTLEMENT_DETAIL.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.GENERATE_SETTLEMENT_DETAIL){
				this.generateStatSettlementByDate(date);
				statTaskLog.setStatus(StatTaskStatus.GENERATE_SETTLEMENT_RESULT.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.GENERATE_SETTLEMENT_RESULT){
				this.generateStatServiceSettlementResultByDate(date);
				statTaskLog.setStatus(StatTaskStatus.FINISH.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			LOGGER.debug("End production settlement data. date = {}", date);
		} catch (Exception e) {
			LOGGER.error("production settlement data error, date = {} error = {}", date, e);
		} finally{
			//执行结束，解锁
			statTaskLog.setIslock(StatTaskLock.UNLOCK.getCode());
			statTransactionProvider.updateStatTaskLog(statTaskLog);
		}
		
		return statTaskLog;
	}
	
	/**
	 * 同步物业缴费订单到结算订单表
	 * @param date
	 */
	private void syncPmsyToStatOrderByDate(String date){

		if(StringUtils.isEmpty(date)){
			LOGGER.error("syncPmsyToStatOrderByDate parameter date is null, date = {}", date);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"parameter date is null.");
		}
		
		//先删除数据，以免重复
		statTransactionProvider.deleteStatOrderByDate(date, SettlementResourceType.PMSY.getCode());
		
		
		Timestamp startDate = Timestamp.valueOf(date + " 00:00:00");
		Timestamp endDate = Timestamp.valueOf(date + " 00:00:00");
		endDate.setDate(endDate.getDate() + 1);
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, 10);
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<Byte> statuses = new ArrayList<Byte>();
		statuses.add(PmsyOrderStatus.UNPAID.getCode());
		statuses.add(PmsyOrderStatus.PAID.getCode());
		while (true) {
			//物业缴费交易订单数据同步
			List<PmsyOrder> pmsyOrders = pmsyProvider.listPmsyOrders(pageSize, startDate, endDate,statuses, locator);
			for (PmsyOrder pmsyOrder : pmsyOrders) {
				StatOrder statOrder = new StatOrder();
				if(PmsyOwnerType.fromCode(pmsyOrder.getOwnerType()) == PmsyOwnerType.COMMUNITY){
					statOrder.setCommunityId(pmsyOrder.getOwnerId());
				}else{
					statOrder.setCommunityId(0L);
				}
				statOrder.setNamespaceId(pmsyOrder.getNamespaceId());
				statOrder.setOrderAmount(pmsyOrder.getOrderAmount());
				statOrder.setOrderDate(date);
				statOrder.setOrderNo(pmsyOrder.getId().toString());
				statOrder.setOrderTime(pmsyOrder.getCreateTime());
				statOrder.setOrderType(SettlementOrderType.TRANSACTION.getCode());
				statOrder.setPayerUid(pmsyOrder.getCreatorUid());
				statOrder.setResourceType(SettlementResourceType.PMSY.getCode());
				statOrder.setResourceId(0L);
				
				//转换成结算订单数据的状态
				if(PmsyOrderStatus.fromCode(pmsyOrder.getStatus()) == PmsyOrderStatus.PAID){
					statOrder.setStatus(SettlementStatOrderStatus.PAID.getCode());
				}else if(PmsyOrderStatus.fromCode(pmsyOrder.getStatus()) == PmsyOrderStatus.UNPAID){
					statOrder.setStatus(SettlementStatOrderStatus.WAITING.getCode());
				}
				statTransactionProvider.createStatOrder(statOrder);
			}
			
			//没查出记录或者anchor为null 代表已经是尾页了
			if(pmsyOrders.size() == 0 || null == locator.getAnchor()){
				break;
			}
		}
		
		while (true) {
			//物业缴费退款订单数据同步
		}
		
	}
	
	/**
	 * 同步电商订单订单到结算订单表
	 * @param date
	 */
	private void syncShopToStatOrderByDate(String date){
		
	}
	
	/**
	 * 同步停车充值订单到结算订单表
	 * @param date
	 */
	private void syncParkingRechargeToStatOrderByDate(String date){
		if(StringUtils.isEmpty(date)){
			LOGGER.error("syncParkingRechargeToStatOrderByDate parameter date is null, date = {}", date);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"parameter date is null.");
		}
		
		//先删除数据，以免重复
		statTransactionProvider.deleteStatOrderByDate(date, SettlementResourceType.PARKING_RECHARGE.getCode());
		
		Timestamp startDate = Timestamp.valueOf(date + " 00:00:00");
		Timestamp endDate = Timestamp.valueOf(date + " 00:00:00");
		endDate.setDate(endDate.getDate() + 1);
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, 10);
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<Byte> statuses = new ArrayList<Byte>();
		statuses.add(ParkingRechargeOrderStatus.UNPAID.getCode());
		statuses.add(ParkingRechargeOrderStatus.PAID.getCode());
		
		while (true) {
			//停车充值交易订单数据同步
			List<ParkingRechargeOrder> parkingRechargeOrders = parkingProvider.listParkingRechargeOrders(pageSize, startDate, endDate,statuses, locator);
			for (ParkingRechargeOrder parkingRechargeOrder : parkingRechargeOrders) {
				StatOrder statOrder = new StatOrder();
				if(PmsyOwnerType.fromCode(parkingRechargeOrder.getOwnerType()) == PmsyOwnerType.COMMUNITY){
					statOrder.setCommunityId(parkingRechargeOrder.getOwnerId());
				}else{
					statOrder.setCommunityId(0L);
				}
				User user = userProvider.findUserById(parkingRechargeOrder.getPayerUid());
				statOrder.setNamespaceId(user.getNamespaceId());
				statOrder.setOrderAmount(parkingRechargeOrder.getPrice());
				statOrder.setOrderDate(date);
				statOrder.setOrderNo(parkingRechargeOrder.getOrderNo().toString());
				statOrder.setOrderTime(parkingRechargeOrder.getRechargeTime());
				statOrder.setOrderType(SettlementOrderType.TRANSACTION.getCode());
				statOrder.setPayerUid(parkingRechargeOrder.getPayerUid());
				statOrder.setResourceType(SettlementResourceType.PARKING_RECHARGE.getCode());
				statOrder.setResourceId(0L);
				
				//转换成结算订单数据的状态
				if(ParkingRechargeOrderStatus.fromCode(parkingRechargeOrder.getStatus()) == ParkingRechargeOrderStatus.PAID){
					statOrder.setStatus(SettlementStatOrderStatus.PAID.getCode());
				}else if(ParkingRechargeOrderStatus.fromCode(parkingRechargeOrder.getStatus()) == ParkingRechargeOrderStatus.UNPAID){
					statOrder.setStatus(SettlementStatOrderStatus.WAITING.getCode());
				}
				statTransactionProvider.createStatOrder(statOrder);
			}
			
			//没查出记录或者anchor为null 代表已经是尾页了
			if(parkingRechargeOrders.size() == 0 || null == locator.getAnchor()){
				break;
			}
		}
		
		while (true) {
			//停车充值退款订单数据同步
		}
	}
	
	/**
	 * 同步资源预定订单到结算订单表
	 * @param date
	 */
	private void syncRentalSiteToStatOrderByDate(String date){
		
	}
	
	/**
	 * 同步一卡通订单到结算订单表
	 * @param date
	 */
	private void syncPaymentToStatOrderByDate(String date){
		if(StringUtils.isEmpty(date)){
			LOGGER.error("syncPaymentToStatOrderByDate parameter date is null, date = {}", date);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"parameter date is null.");
		}
		
		//先删除数据，以免重复
		statTransactionProvider.deleteStatOrderByDate(date, SettlementResourceType.PAYMENT_CARD.getCode());
		
		Timestamp startDate = Timestamp.valueOf(date + " 00:00:00");
		Timestamp endDate = Timestamp.valueOf(date + " 00:00:00");
		endDate.setDate(endDate.getDate() + 1);
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, 10);
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<Byte> statuses = new ArrayList<Byte>();
		statuses.add(CardOrderStatus.UNPAID.getCode());
		statuses.add(CardOrderStatus.PAID.getCode());
		
		while (true) {
			//一卡通交易订单数据同步
			List<PaymentCardRechargeOrder> paymentCardRechargeOrders = paymentCardProvider.listPaymentCardRechargeOrders(pageSize, startDate, endDate,statuses, locator);
			for (PaymentCardRechargeOrder paymentCardRechargeOrder : paymentCardRechargeOrders) {
				StatOrder statOrder = new StatOrder();
				if(PmsyOwnerType.fromCode(paymentCardRechargeOrder.getOwnerType()) == PmsyOwnerType.COMMUNITY){
					statOrder.setCommunityId(paymentCardRechargeOrder.getOwnerId());
				}else{
					statOrder.setCommunityId(0L);
				}
				statOrder.setNamespaceId(paymentCardRechargeOrder.getNamespaceId());
				statOrder.setOrderAmount(paymentCardRechargeOrder.getAmount());
				statOrder.setOrderDate(date);
				statOrder.setOrderNo(paymentCardRechargeOrder.getOrderNo().toString());
				statOrder.setOrderTime(paymentCardRechargeOrder.getRechargeTime());
				statOrder.setOrderType(SettlementOrderType.TRANSACTION.getCode());
				statOrder.setPayerUid(paymentCardRechargeOrder.getPayerUid());
				statOrder.setResourceType(SettlementResourceType.PAYMENT_CARD.getCode());
				statOrder.setResourceId(0L);
				
				//转换成结算订单数据的状态
				if(CardOrderStatus.fromCode(paymentCardRechargeOrder.getPayStatus()) == CardOrderStatus.PAID){
					statOrder.setStatus(SettlementStatOrderStatus.PAID.getCode());
				}else if(CardOrderStatus.fromCode(paymentCardRechargeOrder.getPayStatus()) == CardOrderStatus.UNPAID){
					statOrder.setStatus(SettlementStatOrderStatus.WAITING.getCode());
				}
				statTransactionProvider.createStatOrder(statOrder);
			}
			
			//没查出记录或者anchor为null 代表已经是尾页了
			if(paymentCardRechargeOrders.size() == 0 || null == locator.getAnchor()){
				break;
			}
		}
		
		while (true) {
			//一卡通退款订单数据同步
		}
	}
	
	/**
	 * 同步支付平台交易流水到结算交易流水表
	 * @param date
	 */
	private void syncPaidPlatformToStatTransaction(String date){
		
	}
	
	/**
	 * 同步一卡通交易流水到结算交易流水表
	 * @param date
	 */
	private void syncPaymentToStatTransaction(String date){
		if(StringUtils.isEmpty(date)){
			LOGGER.error("syncPaymentToStatTransaction parameter date is null, date = {}", date);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"parameter date is null.");
		}
		
		//先删除数据，以免重复
		statTransactionProvider.deleteStatTransactionByDate(date, SettlementResourceType.PAYMENT_CARD.getCode());
		
		Timestamp startDate = Timestamp.valueOf(date + " 00:00:00");
		Timestamp endDate = Timestamp.valueOf(date + " 00:00:00");
		endDate.setDate(endDate.getDate() + 1);
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, 10);
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<Byte> statuses = new ArrayList<Byte>();
		statuses.add(CardTransactionStatus.PAIDED.getCode());
		
		while (true) {
			//一卡通交易交易数据同步
			List<PaymentCardTransaction> paymentCardTransactions = paymentCardProvider.listCardTransactions(pageSize, startDate, endDate,statuses, locator);
			for (PaymentCardTransaction paymentCardTransaction : paymentCardTransactions) {
				StatOrder statOrder = statTransactionProvider.findStatOrderByOrderNoAndResourceType(paymentCardTransaction.getOrderNo(), SettlementResourceType.PAYMENT_CARD.getCode());
				StatTransaction statTransaction = new StatTransaction();
				if(PmsyOwnerType.fromCode(paymentCardTransaction.getOwnerType()) == PmsyOwnerType.COMMUNITY){
					statTransaction.setCommunityId(paymentCardTransaction.getOwnerId());
				}else{
					statTransaction.setCommunityId(0L);
				}
				statTransaction.setNamespaceId(paymentCardTransaction.getNamespaceId());
				statTransaction.setPayerUid(paymentCardTransaction.getPayerUid());
				if(null != statOrder){
					statTransaction.setOrderAmount(statOrder.getOrderAmount());
					statTransaction.setItemCode(statOrder.getItemCode());
					statTransaction.setVendorCode(statOrder.getVendorCode());
					statTransaction.setPayerUid(statOrder.getPayerUid());
				}
				statTransaction.setResourceType(SettlementResourceType.PAYMENT_CARD.getCode());
				statTransaction.setResourceId(0L);
				statTransaction.setPaidDate(date);
				statTransaction.setServiceType(SettlementServiceType.COMMUNITY_SERVICE.getCode());
				statTransaction.setOrderNo(paymentCardTransaction.getOrderNo());
				statTransaction.setPaidAmount(paymentCardTransaction.getAmount());
				statTransaction.setTransactionNo(paymentCardTransaction.getTransactionNo());
				statTransaction.setFeeRate(BigDecimal.valueOf(0));
				statTransaction.setFeeAmount(statTransaction.getPaidAmount().multiply(statTransaction.getFeeRate()));
				statTransaction.setSettlementAmount(statTransaction.getPaidAmount().subtract(statTransaction.getFeeAmount()));
				statTransaction.setVendorTransactionNo(paymentCardTransaction.getTransactionNo());
				statTransaction.setPaidStatus(SettlementStatTransactionPaidStatus.PAID.getCode());
				statTransaction.setPaidTime(paymentCardTransaction.getTransactionTime());
				statTransactionProvider.createStatOrder(statOrder);
			}
			
			//没查出记录或者anchor为null 代表已经是尾页了
			if(paymentCardTransactions.size() == 0 || null == locator.getAnchor()){
				break;
			}
		}
	}
	
	/**
	 * 同步支付平台退款流水到结算退款流水表
	 * @param date
	 */
	private void syncPaidPlatformToStatRefund(String date){
		
	}
	
	/**
	 * 同步一卡通退款流水到结算退款流水表
	 * @param date
	 */
	private void syncPaymentToStatRefund(String date){
		
	}
	
	/**
	 * 生成结算数据
	 * @param date
	 */
	private void generateStatSettlementByDate(String date){
		List<StatSettlement> transactionSettlements = statTransactionProvider.countStatTransactionSettlement(date);
		
		List<StatSettlement> refundSettlements = statTransactionProvider.countStatRefundSettlement(date);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("generate transaction settlement data size = {}, date = {}", transactionSettlements.size(), date);
			LOGGER.debug("generate refund settlement data size = {}, date = {}", refundSettlements.size(), date);
		}
		dbProvider.execute((TransactionStatus status) -> {
			if(transactionSettlements.size() > 0){
				for (StatSettlement statSettlement : transactionSettlements) {
					statTransactionProvider.createStatSettlement(statSettlement);
				}
				
				for (StatSettlement statSettlement : refundSettlements) {
					Condition cond = Tables.EH_STAT_SETTLEMENTS.NAMESPACE_ID.eq(statSettlement.getNamespaceId());
					cond = cond.and(Tables.EH_STAT_SETTLEMENTS.COMMUNITY_ID.eq(statSettlement.getCommunityId()));
					cond = cond.and(Tables.EH_STAT_SETTLEMENTS.SERVICE_TYPE.eq(statSettlement.getServiceType()));
					cond = cond.and(Tables.EH_STAT_SETTLEMENTS.RESOURCE_TYPE.eq(statSettlement.getResourceType()));
					cond = cond.and(Tables.EH_STAT_SETTLEMENTS.RESOURCE_ID.eq(statSettlement.getResourceId()));
					cond = cond.and(Tables.EH_STAT_SETTLEMENTS.PAID_CHANNEL.eq(statSettlement.getPaidChannel()));
					StatSettlement settlement = statTransactionProvider.findStatSettlementByDate(cond, date);
					
					if(null == settlement){
						statTransactionProvider.createStatSettlement(statSettlement);
					}else{
						settlement.setRefundAmount(statSettlement.getRefundAmount());
						settlement.setRefundCount(statSettlement.getRefundCount());
						settlement.setRefundFeeAmount(statSettlement.getRefundFeeAmount());
						settlement.setRefundFeeRate(statSettlement.getRefundFeeRate());
						settlement.setRefundSettlementAmount(statSettlement.getRefundSettlementAmount());
						statTransactionProvider.updateStatSettlement(statSettlement);
					}
				}
				
			}else{
				for (StatSettlement statSettlement : refundSettlements) {
					statTransactionProvider.createStatSettlement(statSettlement);
				}
			}
			return null;
		});
	}
	
	/**
	 * 生成结算数据结果
	 * @param date
	 */
	private void generateStatServiceSettlementResultByDate(String date){
		List<StatServiceSettlementResult> alipaySettlementResults = statTransactionProvider.countStatServiceSettlementResult(PaidChannel.ALIPAY.getCode(), date);
		List<StatServiceSettlementResult> wechatSettlementResults = statTransactionProvider.countStatServiceSettlementResult(PaidChannel.WECHAT.getCode(), date);
		List<StatServiceSettlementResult> paymentSettlementResults = statTransactionProvider.countStatServiceSettlementResult(PaidChannel.PAYMENT.getCode(), date);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("generate alipay settlement data size = {}, date = {}", alipaySettlementResults.size(), date);
			LOGGER.debug("generate wechat settlement data size = {}, date = {}", wechatSettlementResults.size(), date);
			LOGGER.debug("generate payment settlement data size = {}, date = {}", paymentSettlementResults.size(), date);
		}
		if(alipaySettlementResults.size() > 0){
			for (StatServiceSettlementResult statServiceSettlementResult : alipaySettlementResults) {
				statTransactionProvider.createStatServiceSettlementResult(statServiceSettlementResult);
			}
			
			for (StatServiceSettlementResult statServiceSettlementResult : wechatSettlementResults) {
				Condition cond = Tables.EH_STAT_SETTLEMENTS.NAMESPACE_ID.eq(statServiceSettlementResult.getNamespaceId());
				cond = cond.and(Tables.EH_STAT_SETTLEMENTS.COMMUNITY_ID.eq(statServiceSettlementResult.getCommunityId()));
				cond = cond.and(Tables.EH_STAT_SETTLEMENTS.SERVICE_TYPE.eq(statServiceSettlementResult.getServiceType()));
				cond = cond.and(Tables.EH_STAT_SETTLEMENTS.RESOURCE_TYPE.eq(statServiceSettlementResult.getResourceType()));
				cond = cond.and(Tables.EH_STAT_SETTLEMENTS.RESOURCE_ID.eq(statServiceSettlementResult.getResourceId()));
				StatServiceSettlementResult settlementResult = statTransactionProvider.findStatServiceSettlementResultDate(cond, date);
				if(null == settlementResult){
					statTransactionProvider.createStatServiceSettlementResult(statServiceSettlementResult);
				}
			}
			
			for (StatServiceSettlementResult statServiceSettlementResult : paymentSettlementResults) {
				Condition cond = Tables.EH_STAT_SETTLEMENTS.NAMESPACE_ID.eq(statServiceSettlementResult.getNamespaceId());
				cond = cond.and(Tables.EH_STAT_SETTLEMENTS.COMMUNITY_ID.eq(statServiceSettlementResult.getCommunityId()));
				cond = cond.and(Tables.EH_STAT_SETTLEMENTS.SERVICE_TYPE.eq(statServiceSettlementResult.getServiceType()));
				cond = cond.and(Tables.EH_STAT_SETTLEMENTS.RESOURCE_TYPE.eq(statServiceSettlementResult.getResourceType()));
				cond = cond.and(Tables.EH_STAT_SETTLEMENTS.RESOURCE_ID.eq(statServiceSettlementResult.getResourceId()));
				StatServiceSettlementResult settlementResult = statTransactionProvider.findStatServiceSettlementResultDate(cond, date);
				if(null == settlementResult){
					statTransactionProvider.createStatServiceSettlementResult(statServiceSettlementResult);
				}
			}
		}else if(wechatSettlementResults.size() > 0){
			for (StatServiceSettlementResult statServiceSettlementResult : wechatSettlementResults) {
				statTransactionProvider.createStatServiceSettlementResult(statServiceSettlementResult);
			}
			
			for (StatServiceSettlementResult statServiceSettlementResult : paymentSettlementResults) {
				Condition cond = Tables.EH_STAT_SETTLEMENTS.NAMESPACE_ID.eq(statServiceSettlementResult.getNamespaceId());
				cond = cond.and(Tables.EH_STAT_SETTLEMENTS.COMMUNITY_ID.eq(statServiceSettlementResult.getCommunityId()));
				cond = cond.and(Tables.EH_STAT_SETTLEMENTS.SERVICE_TYPE.eq(statServiceSettlementResult.getServiceType()));
				cond = cond.and(Tables.EH_STAT_SETTLEMENTS.RESOURCE_TYPE.eq(statServiceSettlementResult.getResourceType()));
				cond = cond.and(Tables.EH_STAT_SETTLEMENTS.RESOURCE_ID.eq(statServiceSettlementResult.getResourceId()));
				StatServiceSettlementResult settlementResult = statTransactionProvider.findStatServiceSettlementResultDate(cond, date);
				if(null == settlementResult){
					statTransactionProvider.createStatServiceSettlementResult(statServiceSettlementResult);
				}
			}
		}else if(paymentSettlementResults.size() > 0){
			for (StatServiceSettlementResult statServiceSettlementResult : paymentSettlementResults) {
				statTransactionProvider.createStatServiceSettlementResult(statServiceSettlementResult);
			}
		}
	}

	public static void main(String[] args) {
		try {
			System.out.println("2016-07-20".compareTo("2015-08-19"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
