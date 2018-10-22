package com.everhomes.asset.statistic;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.asset.AssetPaymentBillDeleteFlag;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPaymentBills;
import com.everhomes.server.schema.tables.daos.EhPaymentBillStatisticCommunityDao;
import com.everhomes.server.schema.tables.pojos.EhPaymentBillStatisticCommunity;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
/**
 * @author created by ycx
 * @date 下午3:52:18
 */
@Component
public class AssetStatisticProviderImpl implements AssetStatisticProvider {

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
 
	public void createStatisticByCommnunity(Integer namespaceId, Long ownerId, String ownerType, String dateStr) {
		//根据namespaceId、ownerId、ownerType、dateStr统计账单相关数据
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhPaymentBillStatisticCommunityDao statisticCommunityDao = new EhPaymentBillStatisticCommunityDao(context.configuration());
		EhPaymentBillStatisticCommunity dto = statisticByCommnunity(namespaceId, ownerId, ownerType, dateStr);
        statisticCommunityDao.insert(dto);
	}

	public boolean checkIsNeedRefreshStatistic(Integer namespaceId, Long ownerId, String ownerType, String dateStr,
			String beforeDateStr) {
		DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<Long> fetch = dslContext.select(Tables.EH_PAYMENT_BILLS.ID)
                .from(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_PAYMENT_BILLS.OWNER_ID.eq(ownerId))
                .and(Tables.EH_PAYMENT_BILLS.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_PAYMENT_BILLS.DATE_STR.eq(dateStr))
                .and(DSL.cast(Tables.EH_PAYMENT_BILLS.UPDATE_TIME, String.class).greaterOrEqual(beforeDateStr + " 00:00:00")
                		.or(DSL.cast(Tables.EH_PAYMENT_BILLS.CREAT_TIME, String.class).greaterOrEqual(beforeDateStr + " 00:00:00")))
                .fetch(Tables.EH_PAYMENT_BILLS.ID);
        if(fetch.size() > 0) return true;
        return false;
	}

	public void updateStatisticByCommnunity(Integer namespaceId, Long ownerId, String ownerType, String dateStr) {
		//根据namespaceId、ownerId、ownerType、dateStr统计账单相关数据
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		com.everhomes.server.schema.tables.EhPaymentBillStatisticCommunity statistic = Tables.EH_PAYMENT_BILL_STATISTIC_COMMUNITY.as("statistic");
		EhPaymentBillStatisticCommunity dto = statisticByCommnunity(namespaceId, ownerId, ownerType, dateStr);
		context.update(statistic)
		    .set(statistic.AMOUNT_RECEIVABLE, dto.getAmountReceivable())
		    .set(statistic.AMOUNT_RECEIVED, dto.getAmountReceived())
		    .set(statistic.AMOUNT_OWED, dto.getAmountOwed())
		    .set(statistic.AMOUNT_RECEIVABLE_WITHOUT_TAX, dto.getAmountReceivableWithoutTax())
		    .set(statistic.AMOUNT_RECEIVED_WITHOUT_TAX, dto.getAmountReceivedWithoutTax())
		    .set(statistic.AMOUNT_OWED_WITHOUT_TAX, dto.getAmountOwedWithoutTax())
		    .set(statistic.TAX_AMOUNT, dto.getTaxAmount())
		    .set(statistic.AMOUNT_EXEMPTION, dto.getAmountExemption())
		    .set(statistic.AMOUNT_SUPPLEMENT, dto.getAmountSupplement())
		    .set(statistic.DUE_DAY_COUNT, dto.getDueDayCount())
		    .set(statistic.NOTICE_TIMES, dto.getNoticeTimes())
		    .where(statistic.NAMESPACE_ID.eq(namespaceId))
		    .and(statistic.OWNER_ID.eq(ownerId))
		    .and(statistic.OWNER_TYPE.eq(ownerType))
		    .and(statistic.DATE_STR.eq(dateStr))
		    .execute();
	}
    
	public EhPaymentBillStatisticCommunity statisticByCommnunity(Integer namespaceId, Long ownerId, String ownerType, String dateStr) {
		//根据namespaceId、ownerId、ownerType、dateStr统计账单相关数据
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		final EhPaymentBillStatisticCommunity[] response = {new EhPaymentBillStatisticCommunity()};
        EhPaymentBills r = Tables.EH_PAYMENT_BILLS.as("r");
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(r);
        query.addSelect(DSL.sum(r.AMOUNT_RECEIVABLE),DSL.sum(r.AMOUNT_RECEIVED),DSL.sum(r.AMOUNT_OWED),
        		DSL.sum(r.AMOUNT_RECEIVABLE_WITHOUT_TAX),DSL.sum(r.AMOUNT_RECEIVED_WITHOUT_TAX),DSL.sum(r.AMOUNT_OWED_WITHOUT_TAX),
        		DSL.sum(r.TAX_AMOUNT),DSL.sum(r.AMOUNT_EXEMPTION),DSL.sum(r.AMOUNT_SUPPLEMENT),
        		DSL.sum(r.DUE_DAY_COUNT), DSL.sum(r.NOTICE_TIMES));
        query.addConditions(r.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(r.OWNER_ID.eq(ownerId));
        query.addConditions(r.OWNER_TYPE.eq(ownerType));
        query.addConditions(r.DATE_STR.eq(dateStr));
        query.addConditions(r.SWITCH.eq((byte)1));//只统计已出账单的已缴和未缴费用
        query.addConditions(r.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
        query.fetch().map(f -> {
        	ListBillStatisticByCommunityDTO convertDTO = convertEhPaymentBills(f, r);
        	response[0] = ConvertHelper.convert(convertDTO, EhPaymentBillStatisticCommunity.class);
        	long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillStatisticCommunity.class));
        	response[0].setId(nextSequence);
        	response[0].setNamespaceId(namespaceId);
        	response[0].setOwnerId(ownerId);
        	response[0].setOwnerType(ownerType);
        	response[0].setDateStr(dateStr);
        	response[0].setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        	response[0].setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        	return null;
        });
        return response[0];
	}
	
	/**
	 * 转换参数
	 */
	private ListBillStatisticByCommunityDTO convertEhPaymentBills(Record f, EhPaymentBills r) {
		ListBillStatisticByCommunityDTO dto = new ListBillStatisticByCommunityDTO();
		BigDecimal amountReceivable = f.getValue(DSL.sum(r.AMOUNT_RECEIVABLE));
    	BigDecimal amountReceived = f.getValue(DSL.sum(r.AMOUNT_RECEIVED));
    	BigDecimal amountOwed = f.getValue(DSL.sum(r.AMOUNT_OWED));
    	BigDecimal amountReceivableWithoutTax = f.getValue(DSL.sum(r.AMOUNT_RECEIVABLE_WITHOUT_TAX));
    	BigDecimal amountReceivedWithoutTax = f.getValue(DSL.sum(r.AMOUNT_RECEIVED_WITHOUT_TAX));
    	BigDecimal amountOwedWithoutTax = f.getValue(DSL.sum(r.AMOUNT_OWED_WITHOUT_TAX));
    	BigDecimal taxAmount = f.getValue(DSL.sum(r.TAX_AMOUNT));
    	BigDecimal amountExemption = f.getValue(DSL.sum(r.AMOUNT_EXEMPTION));
    	BigDecimal amountSupplement = f.getValue(DSL.sum(r.AMOUNT_SUPPLEMENT));
    	BigDecimal dueDayCount = f.getValue(DSL.sum(r.DUE_DAY_COUNT));
    	BigDecimal noticeTimes = f.getValue(DSL.sum(r.NOTICE_TIMES));
    	
    	dto.setAmountReceivable(amountReceivable != null ? amountReceivable : BigDecimal.ZERO);
    	dto.setAmountReceived(amountReceived != null ? amountReceived : BigDecimal.ZERO);
    	dto.setAmountOwed(amountOwed != null ? amountOwed : BigDecimal.ZERO);
    	dto.setAmountReceivableWithoutTax(amountReceivableWithoutTax != null ? amountReceivableWithoutTax : BigDecimal.ZERO);
    	dto.setAmountReceivedWithoutTax(amountReceivedWithoutTax != null ? amountReceivedWithoutTax : BigDecimal.ZERO);
    	dto.setAmountOwedWithoutTax(amountOwedWithoutTax != null ? amountOwedWithoutTax : BigDecimal.ZERO);
    	dto.setTaxAmount(taxAmount != null ? taxAmount : BigDecimal.ZERO);
    	dto.setAmountExemption(amountExemption != null ? amountExemption : BigDecimal.ZERO);
    	dto.setAmountSupplement(amountSupplement != null ? amountSupplement : BigDecimal.ZERO);
    	dto.setDueDayCount(dueDayCount != null ? dueDayCount : BigDecimal.ZERO);
    	dto.setNoticeTimes(noticeTimes != null ? noticeTimes : BigDecimal.ZERO);
    	//收缴率=已收含税金额/应收含税金额  
    	BigDecimal collectionRate = calculateCollecionRate(amountReceivable, amountReceived);
    	dto.setCollectionRate(collectionRate);
		return dto;
	}
	
	/**
	 * 转换参数
	 */
	private ListBillStatisticByCommunityDTO convertEhPaymentBillStatisticCommunity(Record f, com.everhomes.server.schema.tables.EhPaymentBillStatisticCommunity r) {
		ListBillStatisticByCommunityDTO dto = new ListBillStatisticByCommunityDTO();
		BigDecimal amountReceivable = f.getValue(DSL.sum(r.AMOUNT_RECEIVABLE));
    	BigDecimal amountReceived = f.getValue(DSL.sum(r.AMOUNT_RECEIVED));
    	BigDecimal amountOwed = f.getValue(DSL.sum(r.AMOUNT_OWED));
    	BigDecimal amountReceivableWithoutTax = f.getValue(DSL.sum(r.AMOUNT_RECEIVABLE_WITHOUT_TAX));
    	BigDecimal amountReceivedWithoutTax = f.getValue(DSL.sum(r.AMOUNT_RECEIVED_WITHOUT_TAX));
    	BigDecimal amountOwedWithoutTax = f.getValue(DSL.sum(r.AMOUNT_OWED_WITHOUT_TAX));
    	BigDecimal taxAmount = f.getValue(DSL.sum(r.TAX_AMOUNT));
    	BigDecimal amountExemption = f.getValue(DSL.sum(r.AMOUNT_EXEMPTION));
    	BigDecimal amountSupplement = f.getValue(DSL.sum(r.AMOUNT_SUPPLEMENT));
    	BigDecimal dueDayCount = f.getValue(DSL.sum(r.DUE_DAY_COUNT));
    	BigDecimal noticeTimes = f.getValue(DSL.sum(r.NOTICE_TIMES));
    	
    	dto.setAmountReceivable(amountReceivable != null ? amountReceivable : BigDecimal.ZERO);
    	dto.setAmountReceived(amountReceived != null ? amountReceived : BigDecimal.ZERO);
    	dto.setAmountOwed(amountOwed != null ? amountOwed : BigDecimal.ZERO);
    	dto.setAmountReceivableWithoutTax(amountReceivableWithoutTax != null ? amountReceivableWithoutTax : BigDecimal.ZERO);
    	dto.setAmountReceivedWithoutTax(amountReceivedWithoutTax != null ? amountReceivedWithoutTax : BigDecimal.ZERO);
    	dto.setAmountOwedWithoutTax(amountOwedWithoutTax != null ? amountOwedWithoutTax : BigDecimal.ZERO);
    	dto.setTaxAmount(taxAmount != null ? taxAmount : BigDecimal.ZERO);
    	dto.setAmountExemption(amountExemption != null ? amountExemption : BigDecimal.ZERO);
    	dto.setAmountSupplement(amountSupplement != null ? amountSupplement : BigDecimal.ZERO);
    	dto.setDueDayCount(dueDayCount != null ? dueDayCount : BigDecimal.ZERO);
    	dto.setNoticeTimes(noticeTimes != null ? noticeTimes : BigDecimal.ZERO);
    	//收缴率=已收含税金额/应收含税金额  
    	BigDecimal collectionRate = calculateCollecionRate(amountReceivable, amountReceived);
    	dto.setCollectionRate(collectionRate);
		return dto;
	}
	
	
	/**
	 * 计算收缴率
	 * 收缴率=已收含税金额/应收含税金额  
	 */
	private BigDecimal calculateCollecionRate(BigDecimal amountReceivable, BigDecimal amountReceived) {
		BigDecimal collectionRate = BigDecimal.ZERO;
    	//如果应收含税金额为0，那么收缴率是100
    	if(amountReceivable == null || amountReceivable.compareTo(BigDecimal.ZERO) == 0) {
    		collectionRate = new BigDecimal("100");
    	}else {
    		if(amountReceived != null) {
        		collectionRate = amountReceived.divide(amountReceivable, 4, BigDecimal.ROUND_HALF_UP);
        		collectionRate = collectionRate.multiply(new BigDecimal(100));
        	}else {
        		collectionRate = BigDecimal.ZERO;
        	}
    	}
    	return collectionRate;
	}

	public List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunity(Integer pageOffSet, Integer pageSize, 
			Integer namespaceId, List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd) {
		List<ListBillStatisticByCommunityDTO> list = new ArrayList<ListBillStatisticByCommunityDTO>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		com.everhomes.server.schema.tables.EhPaymentBillStatisticCommunity statistic = Tables.EH_PAYMENT_BILL_STATISTIC_COMMUNITY.as("statistic");
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(statistic);
        query.addSelect(statistic.NAMESPACE_ID, statistic.OWNER_ID, statistic.OWNER_TYPE, 
        		DSL.sum(statistic.AMOUNT_RECEIVABLE), DSL.sum(statistic.AMOUNT_RECEIVED), DSL.sum(statistic.AMOUNT_OWED),
        		DSL.sum(statistic.AMOUNT_RECEIVABLE_WITHOUT_TAX), DSL.sum(statistic.AMOUNT_RECEIVED_WITHOUT_TAX), DSL.sum(statistic.AMOUNT_OWED_WITHOUT_TAX),
        		DSL.sum(statistic.TAX_AMOUNT),DSL.sum(statistic.AMOUNT_EXEMPTION),DSL.sum(statistic.AMOUNT_SUPPLEMENT),
        		DSL.sum(statistic.DUE_DAY_COUNT), DSL.sum(statistic.NOTICE_TIMES));
        query.addConditions(statistic.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(statistic.OWNER_ID.in(ownerIdList));
        query.addConditions(statistic.OWNER_TYPE.eq(ownerType));
        if(!org.springframework.util.StringUtils.isEmpty(dateStrBegin)) {
        	query.addConditions(statistic.DATE_STR.greaterOrEqual(dateStrBegin));
        }
        if(!org.springframework.util.StringUtils.isEmpty(dateStrEnd)) {
        	query.addConditions(statistic.DATE_STR.lessOrEqual(dateStrEnd));
        }
        query.addGroupBy(statistic.NAMESPACE_ID, statistic.OWNER_ID, statistic.OWNER_TYPE);
        query.addLimit(pageOffSet,pageSize+1);
        query.fetch().map(f -> {
        	ListBillStatisticByCommunityDTO convertDTO = convertEhPaymentBillStatisticCommunity(f, statistic);
        	ListBillStatisticByCommunityDTO dto  = ConvertHelper.convert(convertDTO, ListBillStatisticByCommunityDTO.class);
        	//TODO 调用资产的接口获取资产相关的统计数据
//        	Integer currentNamespaceId = f.getValue(statistic.NAMESPACE_ID);
//        	Long currentOwnerId = f.getValue(statistic.OWNER_ID);
//        	String currentOwnerType = f.getValue(statistic.OWNER_TYPE);
        	
        	list.add(dto);
        	return null;
        });
		return list;
	}

	public List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunityForProperty(Integer namespaceId,
			List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd) {
		List<ListBillStatisticByCommunityDTO> list = new ArrayList<ListBillStatisticByCommunityDTO>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		com.everhomes.server.schema.tables.EhPaymentBillStatisticCommunity statistic = Tables.EH_PAYMENT_BILL_STATISTIC_COMMUNITY.as("statistic");
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(statistic);
        query.addSelect(statistic.NAMESPACE_ID, statistic.OWNER_ID, statistic.OWNER_TYPE, 
        		DSL.sum(statistic.AMOUNT_RECEIVABLE), DSL.sum(statistic.AMOUNT_RECEIVED), DSL.sum(statistic.AMOUNT_OWED),
        		DSL.sum(statistic.AMOUNT_RECEIVABLE_WITHOUT_TAX), DSL.sum(statistic.AMOUNT_RECEIVED_WITHOUT_TAX), DSL.sum(statistic.AMOUNT_OWED_WITHOUT_TAX),
        		DSL.sum(statistic.TAX_AMOUNT),DSL.sum(statistic.AMOUNT_EXEMPTION),DSL.sum(statistic.AMOUNT_SUPPLEMENT),
        		DSL.sum(statistic.DUE_DAY_COUNT), DSL.sum(statistic.NOTICE_TIMES));
        query.addConditions(statistic.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(statistic.OWNER_ID.in(ownerIdList));
        query.addConditions(statistic.OWNER_TYPE.eq(ownerType));
        if(!org.springframework.util.StringUtils.isEmpty(dateStrBegin)) {
        	query.addConditions(statistic.DATE_STR.greaterOrEqual(dateStrBegin));
        }
        if(!org.springframework.util.StringUtils.isEmpty(dateStrEnd)) {
        	query.addConditions(statistic.DATE_STR.lessOrEqual(dateStrEnd));
        }
        query.addGroupBy(statistic.NAMESPACE_ID, statistic.OWNER_ID, statistic.OWNER_TYPE);
        query.fetch().map(f -> {
        	ListBillStatisticByCommunityDTO convertDTO = convertEhPaymentBillStatisticCommunity(f, statistic);
        	ListBillStatisticByCommunityDTO dto  = ConvertHelper.convert(convertDTO, ListBillStatisticByCommunityDTO.class);
        	
        	dto.setNamespaceId(f.getValue(statistic.NAMESPACE_ID));
        	dto.setOwnerId(f.getValue(statistic.OWNER_ID));
        	dto.setOwnerType(f.getValue(statistic.OWNER_TYPE));
        	list.add(dto);
        	return null;
        });
		return list;
	}

	public ListBillStatisticByCommunityDTO listBillStatisticByCommunityTotal(Integer namespaceId,
			List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd) {
		final ListBillStatisticByCommunityDTO[] response = {new ListBillStatisticByCommunityDTO()};
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		com.everhomes.server.schema.tables.EhPaymentBillStatisticCommunity statistic = Tables.EH_PAYMENT_BILL_STATISTIC_COMMUNITY.as("statistic");
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(statistic);
        query.addSelect(statistic.NAMESPACE_ID, statistic.OWNER_ID, statistic.OWNER_TYPE, 
        		DSL.sum(statistic.AMOUNT_RECEIVABLE), DSL.sum(statistic.AMOUNT_RECEIVED), DSL.sum(statistic.AMOUNT_OWED),
        		DSL.sum(statistic.AMOUNT_RECEIVABLE_WITHOUT_TAX), DSL.sum(statistic.AMOUNT_RECEIVED_WITHOUT_TAX), DSL.sum(statistic.AMOUNT_OWED_WITHOUT_TAX),
        		DSL.sum(statistic.TAX_AMOUNT),DSL.sum(statistic.AMOUNT_EXEMPTION),DSL.sum(statistic.AMOUNT_SUPPLEMENT),
        		DSL.sum(statistic.DUE_DAY_COUNT), DSL.sum(statistic.NOTICE_TIMES));
        query.addConditions(statistic.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(statistic.OWNER_ID.in(ownerIdList));
        query.addConditions(statistic.OWNER_TYPE.eq(ownerType));
        if(!org.springframework.util.StringUtils.isEmpty(dateStrBegin)) {
        	query.addConditions(statistic.DATE_STR.greaterOrEqual(dateStrBegin));
        }
        if(!org.springframework.util.StringUtils.isEmpty(dateStrEnd)) {
        	query.addConditions(statistic.DATE_STR.lessOrEqual(dateStrEnd));
        }
        query.fetch().map(f -> {
        	ListBillStatisticByCommunityDTO convertDTO = convertEhPaymentBillStatisticCommunity(f, statistic);
        	response[0]  = ConvertHelper.convert(convertDTO, ListBillStatisticByCommunityDTO.class);
        	return null;
        });
        return response[0];
	}
    
    
}