package com.everhomes.statistics.transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.jooq.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.Organization;
import com.everhomes.rest.statistics.transaction.PaidChannel;
import com.everhomes.rest.statistics.transaction.SettlementStatTransactionPaidStatus;
import com.everhomes.rest.statistics.transaction.StatServiceStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhStatOrdersDao;
import com.everhomes.server.schema.tables.daos.EhStatRefundsDao;
import com.everhomes.server.schema.tables.daos.EhStatServiceSettlementResultsDao;
import com.everhomes.server.schema.tables.daos.EhStatSettlementsDao;
import com.everhomes.server.schema.tables.daos.EhStatTaskLogsDao;
import com.everhomes.server.schema.tables.daos.EhStatTransactionsDao;
import com.everhomes.server.schema.tables.pojos.EhStatOrders;
import com.everhomes.server.schema.tables.pojos.EhStatRefunds;
import com.everhomes.server.schema.tables.pojos.EhStatServiceSettlementResults;
import com.everhomes.server.schema.tables.pojos.EhStatSettlements;
import com.everhomes.server.schema.tables.pojos.EhStatTaskLogs;
import com.everhomes.server.schema.tables.pojos.EhStatTransactions;
import com.everhomes.server.schema.tables.records.EhStatOrdersRecord;
import com.everhomes.server.schema.tables.records.EhStatRefundsRecord;
import com.everhomes.server.schema.tables.records.EhStatServiceRecord;
import com.everhomes.server.schema.tables.records.EhStatServiceSettlementResultsRecord;
import com.everhomes.server.schema.tables.records.EhStatSettlementsRecord;
import com.everhomes.server.schema.tables.records.EhStatTaskLogsRecord;
import com.everhomes.server.schema.tables.records.EhStatTransactionsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class StatTransactionProviderImpl implements StatTransactionProvider {
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Autowired
	private DbProvider dbProvider;

	@Override
	public void createStatOrder(StatOrder statOrder) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatOrders.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        statOrder.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        statOrder.setId(id);
        EhStatOrdersDao dao = new EhStatOrdersDao(context.configuration());
        dao.insert(statOrder);
	}
	
	@Override
	public void deleteStatOrderByDate(String date, String resourceType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteQuery<EhStatOrdersRecord> delete = context.deleteQuery(Tables.EH_STAT_ORDERS);
		delete.addConditions(Tables.EH_STAT_ORDERS.ORDER_DATE.eq(date));
		delete.addConditions(Tables.EH_STAT_ORDERS.RESOURCE_TYPE.eq(resourceType));
		delete.execute();
	}

	@Override
	public StatOrder findStatOrderByOrderNoAndResourceType(String orderNo, String resourceType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<StatOrder> results = new ArrayList<StatOrder>();
		SelectQuery<EhStatOrdersRecord> query = context.selectQuery(Tables.EH_STAT_ORDERS);
		query.addConditions(Tables.EH_STAT_ORDERS.ORDER_NO.eq(orderNo));
		query.addConditions(Tables.EH_STAT_ORDERS.RESOURCE_TYPE.eq(resourceType));
		query.fetch().map(r -> {
			results.add(ConvertHelper.convert(r, StatOrder.class));
			return null;
		});
		
		if(results.size() > 0){
			return results.get(0);
		}
		return null;
	}

	@Override
	public void createStatTransaction(StatTransaction statTransaction) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatTransactions.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        statTransaction.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        statTransaction.setId(id);
        EhStatTransactionsDao dao = new EhStatTransactionsDao(context.configuration());
        dao.insert(statTransaction);
	}
	
	@Override
	public void deleteStatTransactionByDate(String date, String resourceType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteQuery<EhStatTransactionsRecord> delete = context.deleteQuery(Tables.EH_STAT_TRANSACTIONS);
		delete.addConditions(Tables.EH_STAT_TRANSACTIONS.PAID_DATE.eq(date));
		delete.addConditions(Tables.EH_STAT_TRANSACTIONS.RESOURCE_TYPE.eq(resourceType));
		delete.execute();
	}

	@Override
	public List<StatSettlement> countStatTransactionSettlement(String date) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<StatSettlement> results = new ArrayList<StatSettlement>();
		Condition condition = Tables.EH_STAT_TRANSACTIONS.PAID_DATE.eq(date);
		context.select(
				Tables.EH_STAT_TRANSACTIONS.NAMESPACE_ID,
				Tables.EH_STAT_TRANSACTIONS.COMMUNITY_ID,
				Tables.EH_STAT_TRANSACTIONS.SERVICE_TYPE,
				Tables.EH_STAT_TRANSACTIONS.PAID_DATE,
				Tables.EH_STAT_TRANSACTIONS.RESOURCE_TYPE,
				Tables.EH_STAT_TRANSACTIONS.RESOURCE_ID,
				Tables.EH_STAT_TRANSACTIONS.PAID_CHANNEL,
				Tables.EH_STAT_TRANSACTIONS.PAID_AMOUNT.sum(),
				Tables.EH_STAT_TRANSACTIONS.FEE_RATE,
				Tables.EH_STAT_TRANSACTIONS.FEE_AMOUNT.sum(),
				Tables.EH_STAT_TRANSACTIONS.SETTLEMENT_AMOUNT.sum(),
				Tables.EH_STAT_TRANSACTIONS.TRANSACTION_NO.count())
				.from(Tables.EH_STAT_TRANSACTIONS)
				.where(condition)
				.groupBy(Tables.EH_STAT_TRANSACTIONS.NAMESPACE_ID,
						Tables.EH_STAT_TRANSACTIONS.COMMUNITY_ID,
						Tables.EH_STAT_TRANSACTIONS.SERVICE_TYPE,
						Tables.EH_STAT_TRANSACTIONS.RESOURCE_TYPE,
						Tables.EH_STAT_TRANSACTIONS.RESOURCE_ID,
						Tables.EH_STAT_TRANSACTIONS.PAID_CHANNEL)
				.fetch().map((r) -> {
					StatSettlement statSettlement = new StatSettlement();
					statSettlement.setNamespaceId(Integer.valueOf(r.getValue(0).toString()));
					statSettlement.setCommunityId(Long.valueOf(r.getValue(1).toString()));
					statSettlement.setServiceType(String.valueOf(r.getValue(2)));
					statSettlement.setPaidDate(date);
					statSettlement.setResourceType(String.valueOf(r.getValue(4)));
					statSettlement.setResourceId(String.valueOf(r.getValue(5)));
					statSettlement.setPaidChannel(Byte.valueOf(r.getValue(6).toString()));
					statSettlement.setPaidAmount(new BigDecimal(r.getValue(7).toString()));
					statSettlement.setFeeRate(new BigDecimal(r.getValue(8).toString()));
					statSettlement.setFeeAmount(new BigDecimal(r.getValue(9).toString()));
					statSettlement.setSettlementAmount(new BigDecimal(r.getValue(10).toString()));
					statSettlement.setPaidCount(Long.valueOf(r.getValue(11).toString()));
					results.add(statSettlement);
					return null;
				});
		return results;
	}

	@Override
	public void createStatRefund(StatRefund statRefund) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatRefunds.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        statRefund.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        statRefund.setId(id);
        EhStatRefundsDao dao = new EhStatRefundsDao(context.configuration());
        dao.insert(statRefund);
	}

	@Override
	public void deleteStatRefundByDate(String date, String resourceType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteQuery<EhStatRefundsRecord> delete = context.deleteQuery(Tables.EH_STAT_REFUNDS);
		delete.addConditions(Tables.EH_STAT_REFUNDS.REFUND_DATE.eq(date));
		delete.addConditions(Tables.EH_STAT_REFUNDS.RESOURCE_TYPE.eq(resourceType));
		delete.execute();
	}
	
	@Override
	public List<StatSettlement> countStatRefundSettlement(String date) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<StatSettlement> results = new ArrayList<StatSettlement>();
		Condition condition = Tables.EH_STAT_REFUNDS.REFUND_DATE.eq(date);
		context.select(
				Tables.EH_STAT_REFUNDS.NAMESPACE_ID,
				Tables.EH_STAT_REFUNDS.COMMUNITY_ID,
				Tables.EH_STAT_REFUNDS.SERVICE_TYPE,
				Tables.EH_STAT_REFUNDS.REFUND_DATE,
				Tables.EH_STAT_REFUNDS.RESOURCE_TYPE,
				Tables.EH_STAT_REFUNDS.RESOURCE_ID,
				Tables.EH_STAT_REFUNDS.PAID_CHANNEL,
				Tables.EH_STAT_REFUNDS.REFUND_AMOUNT.sum(),
				Tables.EH_STAT_REFUNDS.FEE_RATE,
				Tables.EH_STAT_REFUNDS.FEE_AMOUNT.sum(),
				Tables.EH_STAT_REFUNDS.SETTLEMENT_AMOUNT.sum(),
				Tables.EH_STAT_REFUNDS.REFUND_NO.count())
				.from(Tables.EH_STAT_REFUNDS)
				.where(condition)
				.groupBy(Tables.EH_STAT_REFUNDS.NAMESPACE_ID,
						Tables.EH_STAT_REFUNDS.COMMUNITY_ID,
						Tables.EH_STAT_REFUNDS.SERVICE_TYPE,
						Tables.EH_STAT_REFUNDS.RESOURCE_TYPE,
						Tables.EH_STAT_REFUNDS.RESOURCE_ID,
						Tables.EH_STAT_REFUNDS.PAID_CHANNEL)
				.fetch().map((r) -> {
					StatSettlement statSettlement = new StatSettlement();
					statSettlement.setNamespaceId(Integer.valueOf(r.getValue(0).toString()));
					statSettlement.setCommunityId(Long.valueOf(r.getValue(1).toString()));
					statSettlement.setServiceType(String.valueOf(r.getValue(2)));
					statSettlement.setPaidDate(r.getValue(3).toString());
					statSettlement.setResourceType(String.valueOf(r.getValue(4)));
					statSettlement.setResourceId(String.valueOf(r.getValue(5)));
					statSettlement.setPaidChannel(Byte.valueOf(r.getValue(6).toString()));
					statSettlement.setRefundAmount(new BigDecimal(r.getValue(7).toString()));
					statSettlement.setRefundFeeRate(new BigDecimal(r.getValue(8).toString()));
					statSettlement.setRefundFeeAmount(new BigDecimal(r.getValue(9).toString()));
					statSettlement.setRefundSettlementAmount(new BigDecimal(r.getValue(10).toString()));
					statSettlement.setRefundCount(Long.valueOf(r.getValue(11).toString()));
					results.add(statSettlement);
					return null;
				});
		return results;
	}

	@Override
	public StatSettlement findStatSettlementByDate(Condition cond, String date) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<StatSettlement> results = new ArrayList<StatSettlement>();
		SelectQuery<EhStatSettlementsRecord> query = context.selectQuery(Tables.EH_STAT_SETTLEMENTS);
		query.addConditions(Tables.EH_STAT_SETTLEMENTS.PAID_DATE.eq(date));
		if(null != cond){
			query.addConditions(cond);
		}
		
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, StatSettlement.class));
			return null;
		});
		
		if(results.size() > 0){
			return results.get(0);
		}
		return null;
	}

	@Override
	public void createStatSettlement(StatSettlement statSettlement) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatSettlements.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        statSettlement.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        statSettlement.setId(id);
        EhStatSettlementsDao dao = new EhStatSettlementsDao(context.configuration());
        dao.insert(statSettlement);
	}
	
	@Override
	public void deleteStatSettlementByDate(String date) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteQuery<EhStatSettlementsRecord> delete = context.deleteQuery(Tables.EH_STAT_SETTLEMENTS);
		delete.addConditions(Tables.EH_STAT_SETTLEMENTS.PAID_DATE.eq(date));
		delete.execute();
	}

	@Override
	public void updateStatSettlement(StatSettlement statSettlement) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhStatSettlementsDao dao = new EhStatSettlementsDao(context.configuration());
		statSettlement.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		dao.update(statSettlement);
	}

	@Override
	public List<StatServiceSettlementResult> countStatServiceSettlementResult(Byte paidChannel, String date) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<StatServiceSettlementResult> results = new ArrayList<StatServiceSettlementResult>();
		Condition condition = Tables.EH_STAT_SETTLEMENTS.PAID_DATE.eq(date);
		if(null != PaidChannel.fromCode(paidChannel)){
			condition = condition.and(Tables.EH_STAT_SETTLEMENTS.PAID_CHANNEL.eq(paidChannel));
		}
		SelectQuery<EhStatSettlementsRecord> query = context.selectQuery(Tables.EH_STAT_SETTLEMENTS);
		query.addConditions(condition);
		query.fetch().map((r) -> {
			StatServiceSettlementResult statServiceSettlementResult = ConvertHelper.convert(r, StatServiceSettlementResult.class);
			
			if(PaidChannel.fromCode(paidChannel) == PaidChannel.ALIPAY){
				statServiceSettlementResult.setAlipayPaidAmount(r.getPaidAmount());
				statServiceSettlementResult.setAlipayRefundAmount(r.getRefundAmount());
				statServiceSettlementResult.setAlipayPaidCount(r.getPaidCount());
				statServiceSettlementResult.setAlipayRefundCount(r.getRefundCount());
			}else if(PaidChannel.fromCode(paidChannel) == PaidChannel.WECHAT){
				statServiceSettlementResult.setWechatPaidAmount(r.getPaidAmount());
				statServiceSettlementResult.setWechatRefundAmount(r.getRefundAmount());
				statServiceSettlementResult.setWechatPaidCount(r.getPaidCount());
				statServiceSettlementResult.setWechatRefundCount(r.getRefundCount());
			}else if(PaidChannel.fromCode(paidChannel) == PaidChannel.PAYMENT){
				statServiceSettlementResult.setPaymentCardPaidAmount(r.getPaidAmount());
				statServiceSettlementResult.setPaymentCardRefundAmount(r.getRefundAmount());
				statServiceSettlementResult.setPaymentCardPaidCount(r.getPaidCount());
				statServiceSettlementResult.setPaymentCardRefundCount(r.getRefundCount());
			}else{
				statServiceSettlementResult.setTotalPaidCount(r.getPaidCount());
				statServiceSettlementResult.setTotalRefundCount(r.getRefundCount());
				statServiceSettlementResult.setTotalPaidAmount(r.getPaidAmount());
				statServiceSettlementResult.setTotalRefundAmount(r.getRefundAmount());
			}
			results.add(statServiceSettlementResult);
			return null;
		});
		
		return results;
	}
	
	@Override
	public void createStatServiceSettlementResult(
			StatServiceSettlementResult statServiceSettlementResult) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatServiceSettlementResults.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        statServiceSettlementResult.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        statServiceSettlementResult.setId(id);
        EhStatServiceSettlementResultsDao dao = new EhStatServiceSettlementResultsDao(context.configuration());
        dao.insert(statServiceSettlementResult);
	}
	
	@Override
	public void deleteStatServiceSettlementResultByDate(String date) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteQuery<EhStatServiceSettlementResultsRecord> delete = context.deleteQuery(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS);
		delete.addConditions(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.PAID_DATE.eq(date));
		delete.execute();
	}

	@Override
	public void updateStatServiceSettlementResult(
			StatServiceSettlementResult statServiceSettlementResult) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhStatServiceSettlementResultsDao dao = new EhStatServiceSettlementResultsDao(context.configuration());
		statServiceSettlementResult.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		dao.update(statServiceSettlementResult);
	}

	@Override
	public StatServiceSettlementResult findStatServiceSettlementResultDate(
			Condition cond, String date) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<StatServiceSettlementResult> results = new ArrayList<StatServiceSettlementResult>();
		SelectQuery<EhStatServiceSettlementResultsRecord> query = context.selectQuery(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS);
		query.addConditions(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.PAID_DATE.eq(date));
		if(null != cond){
			query.addConditions(cond);
		}
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, StatServiceSettlementResult.class));
			return null;
		});
		
		if(results.size() > 0){
			return results.get(0);
		}
		return null;
	}
	
	@Override
	public List<StatServiceSettlementResult> listStatServiceSettlementResult(
			Condition cond, String startDate, String endDate) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<StatServiceSettlementResult> results = new ArrayList<StatServiceSettlementResult>();
		Condition condition = Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.PAID_DATE.ge(startDate);
		condition = condition.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.PAID_DATE.le(endDate));
		if(null != cond){
			condition = condition.and(cond);
		}
		
		context.select(
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.SERVICE_TYPE,
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.COMMUNITY_ID,
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_TYPE,
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_ID,
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.ALIPAY_PAID_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.ALIPAY_REFUND_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.WECHAT_PAID_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.WECHAT_REFUND_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.PAYMENT_CARD_PAID_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.PAYMENT_CARD_REFUND_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.TOTAL_PAID_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.TOTAL_REFUND_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.TOTAL_PAID_COUNT.sum())
				.from(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS)
				.where(condition)
				.groupBy(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.SERVICE_TYPE,Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_TYPE,Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_ID)
				.fetch().map((r) -> {
					StatServiceSettlementResult statServiceSettlementResult = new StatServiceSettlementResult();
					statServiceSettlementResult.setServiceType(String.valueOf(r.getValue(0)));
					statServiceSettlementResult.setCommunityId(Long.valueOf(r.getValue(1).toString()));
					statServiceSettlementResult.setResourceType(String.valueOf(r.getValue(2)));
					statServiceSettlementResult.setResourceId(String.valueOf(r.getValue(3)));
					statServiceSettlementResult.setAlipayPaidAmount(new BigDecimal(r.getValue(4).toString()));
					statServiceSettlementResult.setAlipayRefundAmount(new BigDecimal(r.getValue(5).toString()));
					statServiceSettlementResult.setWechatPaidAmount(new BigDecimal(r.getValue(6).toString()));
					statServiceSettlementResult.setWechatRefundAmount(new BigDecimal(r.getValue(7).toString()));
					statServiceSettlementResult.setPaymentCardPaidAmount(new BigDecimal(r.getValue(8).toString()));
					statServiceSettlementResult.setPaymentCardRefundAmount(new BigDecimal(r.getValue(9).toString()));
					statServiceSettlementResult.setTotalPaidAmount(new BigDecimal(r.getValue(10).toString()));
					statServiceSettlementResult.setTotalRefundAmount(new BigDecimal(r.getValue(11).toString()));
					results.add(statServiceSettlementResult);
					return null;
				});
		return results;
	}

	@Override
	public List<StatServiceSettlementResult> listCountStatServiceSettlementResultByService(
			Condition cond, String startDate, String endDate) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<StatServiceSettlementResult> results = new ArrayList<StatServiceSettlementResult>();
		Condition condition = Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.PAID_DATE.ge(startDate);
		condition = condition.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.PAID_DATE.le(endDate));
		if(null != cond){
			condition = condition.and(cond);
		}
		context.select(
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.SERVICE_TYPE,
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.ALIPAY_PAID_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.ALIPAY_REFUND_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.WECHAT_PAID_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.WECHAT_REFUND_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.PAYMENT_CARD_PAID_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.PAYMENT_CARD_REFUND_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.TOTAL_PAID_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.TOTAL_REFUND_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.TOTAL_PAID_COUNT.sum())
				.from(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS)
				.where(condition)
				.groupBy(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.SERVICE_TYPE)
				.fetch().map((r) -> {
					StatServiceSettlementResult statServiceSettlementResult = new StatServiceSettlementResult();
					statServiceSettlementResult.setServiceType(String.valueOf(r.getValue(0)));
					statServiceSettlementResult.setAlipayPaidAmount(new BigDecimal(r.getValue(1).toString()));
					statServiceSettlementResult.setAlipayRefundAmount(new BigDecimal(r.getValue(2).toString()));
					statServiceSettlementResult.setWechatPaidAmount(new BigDecimal(r.getValue(3).toString()));
					statServiceSettlementResult.setWechatRefundAmount(new BigDecimal(r.getValue(4).toString()));
					statServiceSettlementResult.setPaymentCardPaidAmount(new BigDecimal(r.getValue(5).toString()));
					statServiceSettlementResult.setPaymentCardRefundAmount(new BigDecimal(r.getValue(6).toString()));
					statServiceSettlementResult.setTotalPaidAmount(new BigDecimal(r.getValue(7).toString()));
					statServiceSettlementResult.setTotalRefundAmount(new BigDecimal(r.getValue(8).toString()));
					statServiceSettlementResult.setTotalPaidCount(Long.valueOf(r.getValue(9).toString()));
					results.add(statServiceSettlementResult);
					return null;
				});
		return results;
	}
	
	@Override
	public StatServiceSettlementResult getStatServiceSettlementResultTotal(
			Condition cond, String startDate, String endDate) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<StatServiceSettlementResult> results = new ArrayList<StatServiceSettlementResult>();
		Condition condition = Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.PAID_DATE.ge(startDate);
		condition = condition.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.PAID_DATE.le(endDate));
		if(null != cond){
			condition = condition.and(cond);
		}
		
		context.select(
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.ALIPAY_PAID_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.ALIPAY_REFUND_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.WECHAT_PAID_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.WECHAT_REFUND_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.PAYMENT_CARD_PAID_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.PAYMENT_CARD_REFUND_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.TOTAL_PAID_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.TOTAL_REFUND_AMOUNT.sum(),
				Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.TOTAL_PAID_COUNT.sum())
				.from(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS)
				.where(condition)
				.fetch().map((r) -> {
					StatServiceSettlementResult statServiceSettlementResult = new StatServiceSettlementResult();
					statServiceSettlementResult.setAlipayPaidAmount(new BigDecimal(StringUtils.defaultIfNull(r.getValue(0), "0.00").toString()));
					statServiceSettlementResult.setAlipayRefundAmount(new BigDecimal(StringUtils.defaultIfNull(r.getValue(1), "0.00").toString()));
					statServiceSettlementResult.setWechatPaidAmount(new BigDecimal(StringUtils.defaultIfNull(r.getValue(2), "0.00").toString()));
					statServiceSettlementResult.setWechatRefundAmount(new BigDecimal(StringUtils.defaultIfNull(r.getValue(3), "0.00").toString()));
					statServiceSettlementResult.setPaymentCardPaidAmount(new BigDecimal(StringUtils.defaultIfNull(r.getValue(4), "0.00").toString()));
					statServiceSettlementResult.setPaymentCardRefundAmount(new BigDecimal(StringUtils.defaultIfNull(r.getValue(5), "0.00").toString()));
					statServiceSettlementResult.setTotalPaidAmount(new BigDecimal(StringUtils.defaultIfNull(r.getValue(6), "0.00").toString()));
					statServiceSettlementResult.setTotalRefundAmount(new BigDecimal(StringUtils.defaultIfNull(r.getValue(7), "0.00").toString()));
					statServiceSettlementResult.setTotalPaidCount(Long.valueOf(StringUtils.defaultIfNull(r.getValue(8), "0").toString()));
					results.add(statServiceSettlementResult);
					return null;
				});
		if(results.size() > 0){
			return results.get(0);
		}
		
		return null;
	}

	@Override
	public void createStatTaskLog(StatTaskLog statTaskLog) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatTaskLogs.class));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhStatTaskLogsDao dao = new EhStatTaskLogsDao(context.configuration());
		statTaskLog.setId(id);
		statTaskLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		dao.insert(statTaskLog);
	}
	
	@Override
	public void updateStatTaskLog(StatTaskLog statTaskLog) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhStatTaskLogsDao dao = new EhStatTaskLogsDao(context.configuration());
		statTaskLog.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		dao.update(statTaskLog);
	}
	
	@Override
	public StatTaskLog findStatTaskLog(String date) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<StatTaskLog> results = new ArrayList<StatTaskLog>();
		SelectQuery<EhStatTaskLogsRecord> query = context.selectQuery(Tables.EH_STAT_TASK_LOGS);
		query.addConditions(Tables.EH_STAT_TASK_LOGS.TASK_NO.eq(date));
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, StatTaskLog.class));
			return null;
		});
		
		if(results.size() > 0){
			return results.get(0);
		}
		
		return null;
	}
	
	@Override
	public List<StatService> listStatServices(Integer namespaceId,
			String ownerType, Long ownerId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<StatService> results = new ArrayList<StatService>();
		SelectQuery<EhStatServiceRecord> query = context.selectQuery(Tables.EH_STAT_SERVICE);
		query.addConditions(Tables.EH_STAT_SERVICE.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_STAT_SERVICE.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_STAT_SERVICE.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_STAT_SERVICE.STATUS.eq(StatServiceStatus.ACTIVE.getCode()));
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, StatService.class));
			return null;
		});
		return results;
	}
	
	@Override
	public List<StatService> listStatServiceGroupByServiceTypes() {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<StatService> results = new ArrayList<StatService>();
		SelectQuery<EhStatServiceRecord> query = context.selectQuery(Tables.EH_STAT_SERVICE);
		query.addConditions(Tables.EH_STAT_SERVICE.STATUS.eq(StatServiceStatus.ACTIVE.getCode()));
		query.addGroupBy(Tables.EH_STAT_SERVICE.SERVICE_TYPE);
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, StatService.class));
			return null;
		});
		return results;
	}
	
	@Override
	public List<StatTransaction> listStatTransactions(CrossShardListingLocator locator, Integer pageSize, String startDate,
			String endDate, String resourceId, String resourceType, Integer namespaceId, Long communityId, String serviceType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<StatTransaction> results = new ArrayList<StatTransaction>();
		Condition condition = Tables.EH_STAT_TRANSACTIONS.PAID_DATE.ge(startDate);
		condition = condition.and(Tables.EH_STAT_TRANSACTIONS.PAID_DATE.le(endDate));
		
		if(null != namespaceId){
			condition = condition.and(Tables.EH_STAT_TRANSACTIONS.NAMESPACE_ID.eq(namespaceId));
		}
		
		
		if(null != locator.getAnchor()){
			condition = condition.and(Tables.EH_STAT_TRANSACTIONS.PAID_TIME.gt(new Timestamp(locator.getAnchor())));
		}
		
		if(null != serviceType){
			condition = condition.and(Tables.EH_STAT_TRANSACTIONS.SERVICE_TYPE.eq(serviceType));
		}
		if(!StringUtils.isEmpty(resourceType)){
			condition = condition.and(Tables.EH_STAT_TRANSACTIONS.RESOURCE_TYPE.eq(resourceType));
		}
		
		if(!StringUtils.isEmpty(resourceId)){
			condition = condition.and(Tables.EH_STAT_TRANSACTIONS.RESOURCE_ID.eq(resourceId));
		}
		SelectQuery<EhStatTransactionsRecord> query = context.selectQuery(Tables.EH_STAT_TRANSACTIONS);
		query.addConditions(condition);
		query.addOrderBy(Tables.EH_STAT_TRANSACTIONS.PAID_TIME);
		query.addLimit(pageSize + 1);
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, StatTransaction.class));
			return null;
		});
		
		locator.setAnchor(null);
		if(results.size() > pageSize){
			results.remove(results.size() -1);
			locator.setAnchor(results.get(results.size() - 1).getPaidTime().getTime());
		}
		
		return results;
	}
	
	@Override
	public List<StatRefund> listStatRefunds(CrossShardListingLocator locator, Integer pageSize, String startDate,
			String endDate, String resourceId, String resourceType, Integer namespaceId, Long communityId, String serviceType) {
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<StatRefund> results = new ArrayList<StatRefund>();
		Condition condition = Tables.EH_STAT_REFUNDS.REFUND_DATE.ge(startDate);
		condition = condition.and(Tables.EH_STAT_REFUNDS.REFUND_DATE.le(endDate));
		
		if(null != namespaceId){
			condition = condition.and(Tables.EH_STAT_REFUNDS.NAMESPACE_ID.eq(namespaceId));
		}
		
		if(null != locator.getAnchor()){
			condition = condition.and(Tables.EH_STAT_REFUNDS.REFUND_TIME.gt(new Timestamp(locator.getAnchor())));
		}
		
		if(null != serviceType){
			condition = condition.and(Tables.EH_STAT_REFUNDS.SERVICE_TYPE.eq(serviceType));
		}
		
		if(!StringUtils.isEmpty(resourceType)){
			condition = condition.and(Tables.EH_STAT_REFUNDS.RESOURCE_TYPE.eq(resourceType));
		}
		
		if(!StringUtils.isEmpty(resourceId)){
			condition = condition.and(Tables.EH_STAT_REFUNDS.RESOURCE_ID.eq(resourceId));
		}
		SelectQuery<EhStatRefundsRecord> query = context.selectQuery(Tables.EH_STAT_REFUNDS);
		query.addConditions(condition);
		query.addOrderBy(Tables.EH_STAT_REFUNDS.REFUND_TIME);
		query.addLimit(pageSize + 1);
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, StatRefund.class));
			return null;
		});
		
		locator.setAnchor(null);
		if(results.size() > pageSize){
			results.remove(results.size() -1);
			locator.setAnchor(results.get(results.size() - 1).getRefundTime().getTime());
		}
		return results;
	}

	/**
	 * 金地取数据使用
	 */
	@Override
	public List<StatTransaction> listBusinessByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
			Long pageAnchor, int pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select().from(Tables.EH_STAT_TRANSACTIONS)
				.where(Tables.EH_STAT_TRANSACTIONS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_STAT_TRANSACTIONS.CREATE_TIME.eq(new Timestamp(timestamp)))   //此表中update_time没用，所以使用create_time
				.and(Tables.EH_STAT_TRANSACTIONS.PAID_STATUS.eq(SettlementStatTransactionPaidStatus.PAID.getCode()))
				.and(Tables.EH_STAT_TRANSACTIONS.ID.gt(pageAnchor))
				.orderBy(Tables.EH_STAT_TRANSACTIONS.ID.asc())
				.limit(pageSize)
				.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, StatTransaction.class));
		}
		return new ArrayList<StatTransaction>();
	}

	/**
	 * 金地取数据使用
	 */
	@Override
	public List<StatTransaction> listBusinessByUpdateTime(Integer namespaceId, Long timestamp, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select().from(Tables.EH_STAT_TRANSACTIONS)
			.where(Tables.EH_STAT_TRANSACTIONS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_STAT_TRANSACTIONS.PAID_STATUS.eq(SettlementStatTransactionPaidStatus.PAID.getCode()))
			.and(Tables.EH_STAT_TRANSACTIONS.CREATE_TIME.gt(new Timestamp(timestamp)))
			.orderBy(Tables.EH_STAT_TRANSACTIONS.CREATE_TIME.asc(), Tables.EH_STAT_TRANSACTIONS.ID.asc())
			.limit(pageSize)
			.fetch();
			
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, StatTransaction.class));
		}
		return new ArrayList<StatTransaction>();
	}
	
	
	
}
