package com.everhomes.asset.statistic;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.asset.AssetPaymentBillDeleteFlag;
import com.everhomes.rest.asset.statistic.ListBillStatisticByAddressCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByAddressDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByBuildingDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPaymentBillItems;
import com.everhomes.server.schema.tables.EhPaymentBills;
import com.everhomes.server.schema.tables.daos.EhPaymentBillStatisticBuildingDao;
import com.everhomes.server.schema.tables.daos.EhPaymentBillStatisticCommunityDao;
import com.everhomes.server.schema.tables.pojos.EhPaymentBillStatisticBuilding;
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
    
    @Autowired
    private AssetProvider assetProvider;
 
	public void createStatisticByCommnunity(Integer namespaceId, Long ownerId, String ownerType, String dateStr) {
		//根据namespaceId、ownerId、ownerType、dateStr统计账单相关数据
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhPaymentBillStatisticCommunityDao statisticCommunityDao = new EhPaymentBillStatisticCommunityDao(context.configuration());
		EhPaymentBillStatisticCommunity dto = statisticByCommnunity(namespaceId, ownerId, ownerType, dateStr);
		long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillStatisticCommunity.class));
        dto.setId(nextSequence);
        dto.setNamespaceId(namespaceId);
        dto.setOwnerId(ownerId);
        dto.setOwnerType(ownerType);
        dto.setDateStr(dateStr);
        dto.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dto.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        statisticCommunityDao.insert(dto);
	}
	
	public void createStatisticByBuilding(Integer namespaceId, Long ownerId, String ownerType, String dateStr,String buildingName) {
		//根据namespaceId、ownerId、ownerType、dateStr、buildingName统计楼宇相关数据
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhPaymentBillStatisticBuildingDao statisticBuildingDao = new EhPaymentBillStatisticBuildingDao(context.configuration());
		EhPaymentBillStatisticBuilding dto = statisticByBuilding(namespaceId, ownerId, ownerType, dateStr, buildingName);
		long nextSequence = this.sequenceProvider.getNextSequence(
				NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillStatisticBuilding.class));
        dto.setId(nextSequence);
        dto.setNamespaceId(namespaceId);
        dto.setOwnerId(ownerId);
        dto.setOwnerType(ownerType);
        dto.setDateStr(dateStr);
        dto.setBuildingName(buildingName);
        dto.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dto.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		statisticBuildingDao.insert(dto);
	}

	/**
	 * 项目维度
	 */
	public boolean checkIsNeedRefreshStatistic(Integer namespaceId, Long ownerId, String ownerType, String dateStr,
			String beforeDateStr) {
		DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhPaymentBills r = Tables.EH_PAYMENT_BILLS.as("r");
        List<Long> fetch = dslContext.select(r.ID)
                .from(r)
                .where(r.NAMESPACE_ID.eq(namespaceId))
                .and(r.OWNER_ID.eq(ownerId))
                .and(r.OWNER_TYPE.eq(ownerType))
                .and(r.DATE_STR.eq(dateStr))
                .and(DSL.cast(r.UPDATE_TIME, String.class).greaterOrEqual(beforeDateStr + " 00:00:00")
                		.or(DSL.cast(r.CREAT_TIME, String.class).greaterOrEqual(beforeDateStr + " 00:00:00")))
                .fetch(r.ID);
        if(fetch.size() > 0) return true;
        return false;
	}
	
	/**
	 * 楼宇维度
	 */
	public boolean checkIsNeedRefreshStatistic(Integer namespaceId, Long ownerId, String ownerType, String dateStr,
			String buildingName, String beforeDateStr) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems r = Tables.EH_PAYMENT_BILL_ITEMS.as("r");
        EhPaymentBills bills = Tables.EH_PAYMENT_BILLS.as("bills");
        List<Long> fetch = dslContext.select(r.ID)
                .from(r)
                .leftOuterJoin(bills)
                .on(r.BILL_ID.eq(bills.ID))
                .where(r.NAMESPACE_ID.eq(namespaceId))
                .and(r.OWNER_ID.eq(ownerId))
                .and(r.OWNER_TYPE.eq(ownerType))
                .and(r.DATE_STR.eq(dateStr))
                .and(r.BUILDING_NAME.eq(buildingName))
                .and(DSL.cast(r.UPDATE_TIME, String.class).greaterOrEqual(beforeDateStr + " 00:00:00")
                		.or(DSL.cast(r.CREATE_TIME, String.class).greaterOrEqual(beforeDateStr + " 00:00:00"))
                		.or(DSL.cast(bills.UPDATE_TIME, String.class).greaterOrEqual(beforeDateStr + " 00:00:00"))
                		.or(DSL.cast(bills.CREAT_TIME, String.class).greaterOrEqual(beforeDateStr + " 00:00:00")))
                .fetch(r.ID);
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
	
	public void updateStatisticByBuilding(Integer namespaceId, Long ownerId, String ownerType, String dateStr, String buildingName) {
		//根据namespaceId、ownerId、ownerType、dateStr、buildingName统计楼宇报表
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		com.everhomes.server.schema.tables.EhPaymentBillStatisticBuilding statistic = Tables.EH_PAYMENT_BILL_STATISTIC_BUILDING.as("statistic");
		EhPaymentBillStatisticBuilding dto = statisticByBuilding(namespaceId, ownerId, ownerType, dateStr, buildingName);
		context.update(statistic)
		    .set(statistic.AMOUNT_RECEIVABLE, dto.getAmountReceivable())
		    .set(statistic.AMOUNT_RECEIVED, dto.getAmountReceived())
		    .set(statistic.AMOUNT_OWED, dto.getAmountOwed())
		    .set(statistic.AMOUNT_RECEIVABLE_WITHOUT_TAX, dto.getAmountReceivableWithoutTax())
		    .set(statistic.AMOUNT_RECEIVED_WITHOUT_TAX, dto.getAmountReceivedWithoutTax())
		    .set(statistic.AMOUNT_OWED_WITHOUT_TAX, dto.getAmountOwedWithoutTax())
		    .set(statistic.TAX_AMOUNT, dto.getTaxAmount())
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
		EhPaymentBillStatisticCommunity dto = new EhPaymentBillStatisticCommunity();
        EhPaymentBills r = Tables.EH_PAYMENT_BILLS.as("r");
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(r);
        //先查询出相关数据，把计算的过程放在代码层，减轻数据库的压力
        query.addSelect(r.AMOUNT_RECEIVABLE, r.AMOUNT_RECEIVED, r.AMOUNT_OWED,
        		r.AMOUNT_RECEIVABLE_WITHOUT_TAX, r.AMOUNT_RECEIVED_WITHOUT_TAX, r.AMOUNT_OWED_WITHOUT_TAX,
        		r.TAX_AMOUNT, r.AMOUNT_EXEMPTION, r.AMOUNT_SUPPLEMENT,
        		r.DUE_DAY_COUNT, r.NOTICE_TIMES);
        query.addConditions(r.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(r.OWNER_ID.eq(ownerId));
        query.addConditions(r.OWNER_TYPE.eq(ownerType));
        query.addConditions(r.DATE_STR.eq(dateStr));
        query.addConditions(r.SWITCH.eq((byte)1));//只统计已出账单的已缴和未缴费用
        query.addConditions(r.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
        ListBillStatisticByCommunityDTO convertDTO = convertEhPaymentBills(query, r);
        dto  = ConvertHelper.convert(convertDTO, EhPaymentBillStatisticCommunity.class);
        return dto;
	}
	
	public EhPaymentBillStatisticBuilding statisticByBuilding(Integer namespaceId, Long ownerId, String ownerType, String dateStr, String buildingName) {
		//根据namespaceId、ownerId、ownerType、dateStr、buildingName统计账单相关数据
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhPaymentBillStatisticBuilding dto = new EhPaymentBillStatisticBuilding();
        EhPaymentBillItems r = Tables.EH_PAYMENT_BILL_ITEMS.as("r");
        EhPaymentBills bills = Tables.EH_PAYMENT_BILLS.as("bills");
        SelectQuery<Record> query = context.selectQuery();
        //先查询出相关数据，把计算的过程放在代码层，减轻数据库的压力
        query.addSelect(r.AMOUNT_RECEIVABLE, r.AMOUNT_RECEIVED, r.AMOUNT_OWED,
        		r.AMOUNT_RECEIVABLE_WITHOUT_TAX, r.AMOUNT_RECEIVED_WITHOUT_TAX, r.AMOUNT_OWED_WITHOUT_TAX,
        		r.TAX_AMOUNT, bills.DUE_DAY_COUNT.as("dueDayCount"), bills.NOTICE_TIMES.as("noticeTimes"));
        query.addFrom(r);
		query.addJoin(bills, JoinType.LEFT_OUTER_JOIN, r.BILL_ID.eq(bills.ID));
        query.addConditions(r.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(r.OWNER_ID.eq(ownerId));
        query.addConditions(r.OWNER_TYPE.eq(ownerType));
        query.addConditions(r.DATE_STR.eq(dateStr));
        query.addConditions(r.BUILDING_NAME.eq(buildingName));
        query.addConditions(bills.SWITCH.eq((byte)1));//只统计已出账单的已缴和未缴费用
        query.addConditions(bills.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
        ListBillStatisticByCommunityDTO convertDTO = convertEhPaymentBillItems(query, r);
        dto  = ConvertHelper.convert(convertDTO, EhPaymentBillStatisticBuilding.class);
        return dto;
	}
	
	/**
	 * 转换参数
	 */
	private ListBillStatisticByCommunityDTO convertEhPaymentBills(SelectQuery<Record> query, EhPaymentBills r) {
		ListBillStatisticByCommunityDTO dto = new ListBillStatisticByCommunityDTO();
		final BigDecimal[] amountReceivableFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountReceivedFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountOwedFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountReceivableWithoutTaxFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountReceivedWithoutTaxFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountOwedWithoutTaxFinal = {BigDecimal.ZERO};
		final BigDecimal[] taxAmountFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountExemptionFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountSupplementFinal = {BigDecimal.ZERO};
		final BigDecimal[] dueDayCountFinal = {BigDecimal.ZERO};
		final BigDecimal[] noticeTimesFinal = {BigDecimal.ZERO};
		query.fetch().map(f -> {
			BigDecimal amountReceivable = f.getValue(r.AMOUNT_RECEIVABLE);
	    	BigDecimal amountReceived = f.getValue(r.AMOUNT_RECEIVED);
	    	BigDecimal amountOwed = f.getValue(r.AMOUNT_OWED);
	    	BigDecimal amountReceivableWithoutTax = f.getValue(r.AMOUNT_RECEIVABLE_WITHOUT_TAX);
	    	BigDecimal amountReceivedWithoutTax = f.getValue(r.AMOUNT_RECEIVED_WITHOUT_TAX);
	    	BigDecimal amountOwedWithoutTax = f.getValue(r.AMOUNT_OWED_WITHOUT_TAX);
	    	BigDecimal taxAmount = f.getValue(r.TAX_AMOUNT);
	    	BigDecimal amountExemption = f.getValue(r.AMOUNT_EXEMPTION);
	    	BigDecimal amountSupplement = f.getValue(r.AMOUNT_SUPPLEMENT);
	    	Long dueDayCount = f.getValue(r.DUE_DAY_COUNT);
	    	Integer noticeTimes = f.getValue(r.NOTICE_TIMES);
        	
	    	amountReceivableFinal[0] = amountReceivableFinal[0].add(amountReceivable != null ? amountReceivable : BigDecimal.ZERO);
	    	amountReceivedFinal[0] = amountReceivedFinal[0].add(amountReceived != null ? amountReceived : BigDecimal.ZERO);
	    	amountOwedFinal[0] = amountOwedFinal[0].add(amountOwed != null ? amountOwed : BigDecimal.ZERO);
	    	amountReceivableWithoutTaxFinal[0] = amountReceivableWithoutTaxFinal[0].add(amountReceivableWithoutTax != null ? amountReceivableWithoutTax : BigDecimal.ZERO);
	    	amountReceivedWithoutTaxFinal[0] = amountReceivedWithoutTaxFinal[0].add(amountReceivedWithoutTax != null ? amountReceivedWithoutTax : BigDecimal.ZERO);
	    	amountOwedWithoutTaxFinal[0] = amountOwedWithoutTaxFinal[0].add(amountOwedWithoutTax != null ? amountOwedWithoutTax : BigDecimal.ZERO);
	    	taxAmountFinal[0] = taxAmountFinal[0].add(taxAmount != null ? taxAmount : BigDecimal.ZERO);
	    	amountExemptionFinal[0] = amountExemptionFinal[0].add(amountExemption != null ? amountExemption : BigDecimal.ZERO);
	    	amountSupplementFinal[0] = amountSupplementFinal[0].add(amountSupplement != null ? amountSupplement : BigDecimal.ZERO);
	    	dueDayCountFinal[0] = dueDayCountFinal[0].add(dueDayCount != null ? new BigDecimal(dueDayCount) : BigDecimal.ZERO);
	    	noticeTimesFinal[0] = noticeTimesFinal[0].add(noticeTimes != null ? new BigDecimal(noticeTimes) : BigDecimal.ZERO);
        	return null;
        });
		//收缴率=已收含税金额/应收含税金额  
    	BigDecimal collectionRate = calculateCollecionRate(amountReceivableFinal[0] , amountReceivedFinal[0]);
    	dto.setAmountReceivable(amountReceivableFinal[0] != null ? amountReceivableFinal[0] : BigDecimal.ZERO);
    	dto.setAmountReceived(amountReceivedFinal[0] != null ? amountReceivedFinal[0] : BigDecimal.ZERO);
    	dto.setAmountOwed(amountOwedFinal[0] != null ? amountOwedFinal[0] : BigDecimal.ZERO);
    	dto.setAmountReceivableWithoutTax(amountReceivableWithoutTaxFinal[0] != null ? amountReceivableWithoutTaxFinal[0] : BigDecimal.ZERO);
    	dto.setAmountReceivedWithoutTax(amountReceivedWithoutTaxFinal[0] != null ? amountReceivedWithoutTaxFinal[0] : BigDecimal.ZERO);
    	dto.setAmountOwedWithoutTax(amountOwedWithoutTaxFinal[0] != null ? amountOwedWithoutTaxFinal[0] : BigDecimal.ZERO);
    	dto.setTaxAmount(taxAmountFinal[0] != null ? taxAmountFinal[0] : BigDecimal.ZERO);
    	dto.setAmountExemption(amountExemptionFinal[0] != null ? amountExemptionFinal[0] : BigDecimal.ZERO);
    	dto.setAmountSupplement(amountSupplementFinal[0] != null ? amountSupplementFinal[0] : BigDecimal.ZERO);
    	dto.setDueDayCount(dueDayCountFinal[0] != null ? dueDayCountFinal[0] : BigDecimal.ZERO);
    	dto.setNoticeTimes(noticeTimesFinal[0] != null ? noticeTimesFinal[0] : BigDecimal.ZERO);
    	dto.setCollectionRate(collectionRate);
		return dto;
	}
	
	private ListBillStatisticByCommunityDTO convertEhPaymentBillItems(SelectQuery<Record> query, EhPaymentBillItems r) {
		ListBillStatisticByCommunityDTO dto = new ListBillStatisticByCommunityDTO();
		final BigDecimal[] amountReceivableFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountReceivedFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountOwedFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountReceivableWithoutTaxFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountReceivedWithoutTaxFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountOwedWithoutTaxFinal = {BigDecimal.ZERO};
		final BigDecimal[] taxAmountFinal = {BigDecimal.ZERO};
		final BigDecimal[] dueDayCountFinal = {BigDecimal.ZERO};
		final BigDecimal[] noticeTimesFinal = {BigDecimal.ZERO};
		query.fetch().map(f -> {
			BigDecimal amountReceivable = f.getValue(r.AMOUNT_RECEIVABLE);
	    	BigDecimal amountReceived = f.getValue(r.AMOUNT_RECEIVED);
	    	BigDecimal amountOwed = f.getValue(r.AMOUNT_OWED);
	    	BigDecimal amountReceivableWithoutTax = f.getValue(r.AMOUNT_RECEIVABLE_WITHOUT_TAX);
	    	BigDecimal amountReceivedWithoutTax = f.getValue(r.AMOUNT_RECEIVED_WITHOUT_TAX);
	    	BigDecimal amountOwedWithoutTax = f.getValue(r.AMOUNT_OWED_WITHOUT_TAX);
	    	BigDecimal taxAmount = f.getValue(r.TAX_AMOUNT);
	    	Long dueDayCount = f.getValue("dueDayCount", Long.class);
	    	Integer noticeTimes = f.getValue("noticeTimes", Integer.class);
        	
	    	amountReceivableFinal[0] = amountReceivableFinal[0].add(amountReceivable != null ? amountReceivable : BigDecimal.ZERO);
	    	amountReceivedFinal[0] = amountReceivedFinal[0].add(amountReceived != null ? amountReceived : BigDecimal.ZERO);
	    	amountOwedFinal[0] = amountOwedFinal[0].add(amountOwed != null ? amountOwed : BigDecimal.ZERO);
	    	amountReceivableWithoutTaxFinal[0] = amountReceivableWithoutTaxFinal[0].add(amountReceivableWithoutTax != null ? amountReceivableWithoutTax : BigDecimal.ZERO);
	    	amountReceivedWithoutTaxFinal[0] = amountReceivedWithoutTaxFinal[0].add(amountReceivedWithoutTax != null ? amountReceivedWithoutTax : BigDecimal.ZERO);
	    	amountOwedWithoutTaxFinal[0] = amountOwedWithoutTaxFinal[0].add(amountOwedWithoutTax != null ? amountOwedWithoutTax : BigDecimal.ZERO);
	    	taxAmountFinal[0] = taxAmountFinal[0].add(taxAmount != null ? taxAmount : BigDecimal.ZERO);
	    	dueDayCountFinal[0] = dueDayCountFinal[0].add(dueDayCount != null ? new BigDecimal(dueDayCount) : BigDecimal.ZERO);
	    	noticeTimesFinal[0] = noticeTimesFinal[0].add(noticeTimes != null ? new BigDecimal(noticeTimes) : BigDecimal.ZERO);
        	return null;
        });
		//收缴率=已收含税金额/应收含税金额  
    	BigDecimal collectionRate = calculateCollecionRate(amountReceivableFinal[0] , amountReceivedFinal[0]);
    	dto.setAmountReceivable(amountReceivableFinal[0] != null ? amountReceivableFinal[0] : BigDecimal.ZERO);
    	dto.setAmountReceived(amountReceivedFinal[0] != null ? amountReceivedFinal[0] : BigDecimal.ZERO);
    	dto.setAmountOwed(amountOwedFinal[0] != null ? amountOwedFinal[0] : BigDecimal.ZERO);
    	dto.setAmountReceivableWithoutTax(amountReceivableWithoutTaxFinal[0] != null ? amountReceivableWithoutTaxFinal[0] : BigDecimal.ZERO);
    	dto.setAmountReceivedWithoutTax(amountReceivedWithoutTaxFinal[0] != null ? amountReceivedWithoutTaxFinal[0] : BigDecimal.ZERO);
    	dto.setAmountOwedWithoutTax(amountOwedWithoutTaxFinal[0] != null ? amountOwedWithoutTaxFinal[0] : BigDecimal.ZERO);
    	dto.setTaxAmount(taxAmountFinal[0] != null ? taxAmountFinal[0] : BigDecimal.ZERO);
    	dto.setDueDayCount(dueDayCountFinal[0] != null ? dueDayCountFinal[0] : BigDecimal.ZERO);
    	dto.setNoticeTimes(noticeTimesFinal[0] != null ? noticeTimesFinal[0] : BigDecimal.ZERO);
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
    	dto.setNamespaceId(f.getValue(r.NAMESPACE_ID));
		return dto;
	}
	
	private ListBillStatisticByBuildingDTO convertEhPaymentBillItemsStatisticBuilding(Record f, com.everhomes.server.schema.tables.EhPaymentBillStatisticBuilding r) {
		ListBillStatisticByBuildingDTO dto = new ListBillStatisticByBuildingDTO();
		BigDecimal amountReceivable = f.getValue(DSL.sum(r.AMOUNT_RECEIVABLE));
    	BigDecimal amountReceived = f.getValue(DSL.sum(r.AMOUNT_RECEIVED));
    	BigDecimal amountOwed = f.getValue(DSL.sum(r.AMOUNT_OWED));
    	BigDecimal amountReceivableWithoutTax = f.getValue(DSL.sum(r.AMOUNT_RECEIVABLE_WITHOUT_TAX));
    	BigDecimal amountReceivedWithoutTax = f.getValue(DSL.sum(r.AMOUNT_RECEIVED_WITHOUT_TAX));
    	BigDecimal amountOwedWithoutTax = f.getValue(DSL.sum(r.AMOUNT_OWED_WITHOUT_TAX));
    	BigDecimal taxAmount = f.getValue(DSL.sum(r.TAX_AMOUNT));
    	BigDecimal dueDayCount = f.getValue(DSL.sum(r.DUE_DAY_COUNT));
    	BigDecimal noticeTimes = f.getValue(DSL.sum(r.NOTICE_TIMES));
    	
    	dto.setAmountReceivable(amountReceivable != null ? amountReceivable : BigDecimal.ZERO);
    	dto.setAmountReceived(amountReceived != null ? amountReceived : BigDecimal.ZERO);
    	dto.setAmountOwed(amountOwed != null ? amountOwed : BigDecimal.ZERO);
    	dto.setAmountReceivableWithoutTax(amountReceivableWithoutTax != null ? amountReceivableWithoutTax : BigDecimal.ZERO);
    	dto.setAmountReceivedWithoutTax(amountReceivedWithoutTax != null ? amountReceivedWithoutTax : BigDecimal.ZERO);
    	dto.setAmountOwedWithoutTax(amountOwedWithoutTax != null ? amountOwedWithoutTax : BigDecimal.ZERO);
    	dto.setTaxAmount(taxAmount != null ? taxAmount : BigDecimal.ZERO);
    	dto.setDueDayCount(dueDayCount != null ? dueDayCount : BigDecimal.ZERO);
    	dto.setNoticeTimes(noticeTimes != null ? noticeTimes : BigDecimal.ZERO);
    	//收缴率=已收含税金额/应收含税金额  
    	BigDecimal collectionRate = calculateCollecionRate(amountReceivable, amountReceived);
    	dto.setCollectionRate(collectionRate);
    	dto.setNamespaceId(f.getValue(r.NAMESPACE_ID));
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
        		collectionRate = amountReceived.divide(amountReceivable, 2, BigDecimal.ROUND_HALF_UP);
        		collectionRate = collectionRate.multiply(new BigDecimal(100));
        	}else {
        		collectionRate = BigDecimal.ZERO;
        	}
    	}
    	return collectionRate;
	}
	
	public List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunity(Integer pageOffSet, Integer pageSize, 
			Integer namespaceId, List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd) {
		List<ListBillStatisticByCommunityDTO> list = listBillStatisticByCommunityUtil(pageOffSet, pageSize, 
				namespaceId, ownerIdList, ownerType, dateStrBegin, dateStrEnd);
    	//TODO 调用资产的接口获取资产相关的统计数据
//    	Integer currentNamespaceId = f.getValue(statistic.NAMESPACE_ID);
//    	Long currentOwnerId = f.getValue(statistic.OWNER_ID);
//    	String currentOwnerType = f.getValue(statistic.OWNER_TYPE);
		return list;
	}
	
	public List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunityForProperty(Integer namespaceId,
			List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd) {
		Integer pageOffSet = null;
		Integer pageSize = null;
		List<ListBillStatisticByCommunityDTO> list = listBillStatisticByCommunityUtil(pageOffSet, pageSize, 
				namespaceId, ownerIdList, ownerType, dateStrBegin, dateStrEnd);
		return list;
	}
	
	private List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunityUtil(Integer pageOffSet, Integer pageSize, 
			Integer namespaceId, List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd) {
		List<ListBillStatisticByCommunityDTO> list = new ArrayList<ListBillStatisticByCommunityDTO>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		com.everhomes.server.schema.tables.EhPaymentBillStatisticCommunity statistic = Tables.EH_PAYMENT_BILL_STATISTIC_COMMUNITY.as("statistic");
		SelectQuery<Record> query = context.selectQuery();
        //结果集表的sum不会影响数据库的性能，因为数据量非常小
        query.addSelect(Tables.EH_COMMUNITIES.ID.as("communityId"), Tables.EH_COMMUNITIES.NAMESPACE_ID, statistic.OWNER_ID, statistic.OWNER_TYPE,
        		DSL.sum(statistic.AMOUNT_RECEIVABLE), DSL.sum(statistic.AMOUNT_RECEIVED), DSL.sum(statistic.AMOUNT_OWED),
        		DSL.sum(statistic.AMOUNT_RECEIVABLE_WITHOUT_TAX), DSL.sum(statistic.AMOUNT_RECEIVED_WITHOUT_TAX), DSL.sum(statistic.AMOUNT_OWED_WITHOUT_TAX),
        		DSL.sum(statistic.TAX_AMOUNT),DSL.sum(statistic.AMOUNT_EXEMPTION),DSL.sum(statistic.AMOUNT_SUPPLEMENT),
        		DSL.sum(statistic.DUE_DAY_COUNT), DSL.sum(statistic.NOTICE_TIMES));
        query.addFrom(Tables.EH_COMMUNITIES); //为了兼容没有统计结果的查询
		query.addJoin(statistic, JoinType.LEFT_OUTER_JOIN, Tables.EH_COMMUNITIES.ID.eq(statistic.OWNER_ID));
        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_COMMUNITIES.ID.in(ownerIdList));
        query.addConditions(statistic.NAMESPACE_ID.eq(namespaceId).or(statistic.NAMESPACE_ID.isNull()));
        query.addConditions(statistic.OWNER_ID.in(ownerIdList).or(statistic.OWNER_ID.isNull()));
        query.addConditions(statistic.OWNER_TYPE.eq(ownerType).or(statistic.OWNER_TYPE.isNull()));
        if(!org.springframework.util.StringUtils.isEmpty(dateStrBegin)) {
        	query.addConditions(statistic.DATE_STR.greaterOrEqual(dateStrBegin).or(statistic.DATE_STR.isNull()));
        }
        if(!org.springframework.util.StringUtils.isEmpty(dateStrEnd)) {
        	query.addConditions(statistic.DATE_STR.lessOrEqual(dateStrEnd).or(statistic.DATE_STR.isNull()));
        }
        query.addGroupBy(statistic.NAMESPACE_ID, statistic.OWNER_ID, statistic.OWNER_TYPE);
        if(pageOffSet != null && pageSize != null) {
        	query.addLimit(pageOffSet,pageSize+1);
        }
        query.fetch().map(f -> {
        	ListBillStatisticByCommunityDTO convertDTO = convertEhPaymentBillStatisticCommunity(f, statistic);
        	ListBillStatisticByCommunityDTO dto  = ConvertHelper.convert(convertDTO, ListBillStatisticByCommunityDTO.class);
        	dto.setOwnerId(f.getValue("communityId", Long.class));
        	dto.setOwnerType(f.getValue(statistic.OWNER_TYPE));
        	list.add(dto);
        	return null;
        });
        return list;
	}
	
	public List<ListBillStatisticByBuildingDTO> listBillStatisticByBuilding(Integer pageOffSet, Integer pageSize,
			Integer namespaceId, Long ownerId, String ownerType, String dateStrBegin, String dateStrEnd,
			List<String> buildingNameList) {
		List<ListBillStatisticByBuildingDTO> list = listBillStatisticByBuildingUtil(pageOffSet, pageSize,
				namespaceId, ownerId, ownerType, dateStrBegin, dateStrEnd, buildingNameList);
    	//TODO 调用资产的接口获取资产相关的统计数据
//    	Integer currentNamespaceId = f.getValue(statistic.NAMESPACE_ID);
//    	Long currentOwnerId = f.getValue(statistic.OWNER_ID);
//    	String currentOwnerType = f.getValue(statistic.OWNER_TYPE);
		return list;
	}
	
	public List<ListBillStatisticByBuildingDTO> listBillStatisticByBuildingForProperty(Integer namespaceId,
			Long ownerId, String ownerType, String dateStrBegin, String dateStrEnd, List<String> buildingNameList) {
		Integer pageOffSet = null;
		Integer pageSize = null;
		List<ListBillStatisticByBuildingDTO> list = listBillStatisticByBuildingUtil(pageOffSet, pageSize,
				namespaceId, ownerId, ownerType, dateStrBegin, dateStrEnd, buildingNameList);
		return list;
	}
	
	private List<ListBillStatisticByBuildingDTO> listBillStatisticByBuildingUtil(Integer pageOffSet, Integer pageSize,
			Integer namespaceId, Long ownerId, String ownerType, String dateStrBegin, String dateStrEnd,
			List<String> buildingNameList){
		List<ListBillStatisticByBuildingDTO> list = new ArrayList<ListBillStatisticByBuildingDTO>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		com.everhomes.server.schema.tables.EhPaymentBillStatisticBuilding statistic = Tables.EH_PAYMENT_BILL_STATISTIC_BUILDING.as("statistic");
		SelectQuery<Record> query = context.selectQuery();
        //结果集表的sum不会影响数据库的性能，因为数据量非常小
        query.addSelect(Tables.EH_BUILDINGS.NAME.as("buildingName"), Tables.EH_BUILDINGS.COMMUNITY_ID.as("communityId"), 
        		Tables.EH_BUILDINGS.NAMESPACE_ID, statistic.OWNER_ID, statistic.OWNER_TYPE,
        		DSL.sum(statistic.AMOUNT_RECEIVABLE), DSL.sum(statistic.AMOUNT_RECEIVED), DSL.sum(statistic.AMOUNT_OWED),
        		DSL.sum(statistic.AMOUNT_RECEIVABLE_WITHOUT_TAX), DSL.sum(statistic.AMOUNT_RECEIVED_WITHOUT_TAX), DSL.sum(statistic.AMOUNT_OWED_WITHOUT_TAX),
        		DSL.sum(statistic.TAX_AMOUNT), DSL.sum(statistic.DUE_DAY_COUNT), DSL.sum(statistic.NOTICE_TIMES));
        query.addFrom(Tables.EH_BUILDINGS); //为了兼容没有统计结果的查询
		query.addJoin(statistic, JoinType.LEFT_OUTER_JOIN, Tables.EH_BUILDINGS.NAME.eq(statistic.BUILDING_NAME));
        query.addConditions(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(ownerId));
        query.addConditions(Tables.EH_BUILDINGS.NAME.in(buildingNameList));
        query.addConditions(statistic.NAMESPACE_ID.eq(namespaceId).or(statistic.NAMESPACE_ID.isNull()));
        query.addConditions(statistic.OWNER_ID.eq(ownerId).or(statistic.OWNER_ID.isNull()));
        query.addConditions(statistic.BUILDING_NAME.in(buildingNameList).or(statistic.BUILDING_NAME.isNull()));
        query.addConditions(statistic.OWNER_TYPE.eq(ownerType).or(statistic.OWNER_TYPE.isNull()));
        if(!org.springframework.util.StringUtils.isEmpty(dateStrBegin)) {
        	query.addConditions(statistic.DATE_STR.greaterOrEqual(dateStrBegin).or(statistic.DATE_STR.isNull()));
        }
        if(!org.springframework.util.StringUtils.isEmpty(dateStrEnd)) {
        	query.addConditions(statistic.DATE_STR.lessOrEqual(dateStrEnd).or(statistic.DATE_STR.isNull()));
        }
        query.addGroupBy(statistic.NAMESPACE_ID, statistic.OWNER_ID, statistic.OWNER_TYPE, statistic.BUILDING_NAME);
        if(pageOffSet != null && pageSize != null) {
        	query.addLimit(pageOffSet,pageSize+1);
        }
        query.fetch().map(f -> {
        	ListBillStatisticByBuildingDTO convertDTO = convertEhPaymentBillItemsStatisticBuilding(f, statistic);
        	ListBillStatisticByBuildingDTO dto  = ConvertHelper.convert(convertDTO, ListBillStatisticByBuildingDTO.class);
        	dto.setOwnerId(f.getValue("communityId", Long.class));
        	dto.setOwnerType(f.getValue(statistic.OWNER_TYPE));
        	dto.setBuildingName(f.getValue("buildingName", String.class));
        	list.add(dto);
        	return null;
        });
        return list;
	}

	public ListBillStatisticByCommunityDTO listBillStatisticByCommunityTotal(Integer namespaceId,
			List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd) {
		ListBillStatisticByCommunityDTO dto = listBillStatisticByCommunityTotalUtil(
				namespaceId, ownerIdList, ownerType, dateStrBegin, dateStrEnd);
    	//TODO 调用资产的接口获取资产相关的统计数据
//    	Integer currentNamespaceId = f.getValue(statistic.NAMESPACE_ID);
//    	Long currentOwnerId = f.getValue(statistic.OWNER_ID);
//    	String currentOwnerType = f.getValue(statistic.OWNER_TYPE);
        return dto;
	}
	
	public ListBillStatisticByCommunityDTO listBillStatisticByCommunityTotalForProperty(Integer namespaceId,
			List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd) {
		ListBillStatisticByCommunityDTO dto = listBillStatisticByCommunityTotalUtil(
				namespaceId, ownerIdList, ownerType, dateStrBegin, dateStrEnd);
		return dto;
	}
	
	private ListBillStatisticByCommunityDTO listBillStatisticByCommunityTotalUtil(Integer namespaceId,
			List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd) {
		final ListBillStatisticByCommunityDTO[] response = {new ListBillStatisticByCommunityDTO()};
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		com.everhomes.server.schema.tables.EhPaymentBillStatisticCommunity statistic = Tables.EH_PAYMENT_BILL_STATISTIC_COMMUNITY.as("statistic");
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(statistic);
        //结果集表的sum不会影响数据库的性能，因为数据量非常小
        query.addSelect(statistic.NAMESPACE_ID, 
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
	
	public ListBillStatisticByBuildingDTO listBillStatisticByBuildingTotal(Integer namespaceId, Long ownerId,
			String ownerType, String dateStrBegin, String dateStrEnd, List<String> buildingNameList) {
		ListBillStatisticByBuildingDTO dto = listBillStatisticByBuildingTotalUtil(namespaceId, 
				ownerId, ownerType, dateStrBegin, dateStrEnd, buildingNameList);
    	//TODO 调用资产的接口获取资产相关的统计数据
//    	Integer currentNamespaceId = f.getValue(statistic.NAMESPACE_ID);
//    	Long currentOwnerId = f.getValue(statistic.OWNER_ID);
//    	String currentOwnerType = f.getValue(statistic.OWNER_TYPE);
        return dto;
	}
	
	public ListBillStatisticByBuildingDTO listBillStatisticByBuildingTotalForProperty(Integer namespaceId, Long ownerId,
			String ownerType, String dateStrBegin, String dateStrEnd, List<String> buildingNameList) {
		ListBillStatisticByBuildingDTO dto = listBillStatisticByBuildingTotalUtil(namespaceId, 
				ownerId, ownerType, dateStrBegin, dateStrEnd, buildingNameList);
		return dto;
	}
	
	private ListBillStatisticByBuildingDTO listBillStatisticByBuildingTotalUtil(Integer namespaceId, Long ownerId,
			String ownerType, String dateStrBegin, String dateStrEnd, List<String> buildingNameList) {
		final ListBillStatisticByBuildingDTO[] response = {new ListBillStatisticByBuildingDTO()};
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		com.everhomes.server.schema.tables.EhPaymentBillStatisticBuilding statistic = Tables.EH_PAYMENT_BILL_STATISTIC_BUILDING.as("statistic");
		SelectQuery<Record> query = context.selectQuery();
        //结果集表的sum不会影响数据库的性能，因为数据量非常小
        query.addSelect(statistic.NAMESPACE_ID, statistic.OWNER_ID, statistic.OWNER_TYPE,
        		DSL.sum(statistic.AMOUNT_RECEIVABLE), DSL.sum(statistic.AMOUNT_RECEIVED), DSL.sum(statistic.AMOUNT_OWED),
        		DSL.sum(statistic.AMOUNT_RECEIVABLE_WITHOUT_TAX), DSL.sum(statistic.AMOUNT_RECEIVED_WITHOUT_TAX), DSL.sum(statistic.AMOUNT_OWED_WITHOUT_TAX),
        		DSL.sum(statistic.TAX_AMOUNT), DSL.sum(statistic.DUE_DAY_COUNT), DSL.sum(statistic.NOTICE_TIMES));
        query.addFrom(Tables.EH_BUILDINGS); //为了兼容没有统计结果的查询
		query.addJoin(statistic, JoinType.LEFT_OUTER_JOIN, Tables.EH_BUILDINGS.NAME.eq(statistic.BUILDING_NAME));
        query.addConditions(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(ownerId));
        query.addConditions(Tables.EH_BUILDINGS.NAME.in(buildingNameList));
        query.addConditions(statistic.NAMESPACE_ID.eq(namespaceId).or(statistic.NAMESPACE_ID.isNull()));
        query.addConditions(statistic.OWNER_ID.eq(ownerId).or(statistic.OWNER_ID.isNull()));
        query.addConditions(statistic.BUILDING_NAME.in(buildingNameList).or(statistic.BUILDING_NAME.isNull()));
        query.addConditions(statistic.OWNER_TYPE.eq(ownerType).or(statistic.OWNER_TYPE.isNull()));
        if(!org.springframework.util.StringUtils.isEmpty(dateStrBegin)) {
        	query.addConditions(statistic.DATE_STR.greaterOrEqual(dateStrBegin).or(statistic.DATE_STR.isNull()));
        }
        if(!org.springframework.util.StringUtils.isEmpty(dateStrEnd)) {
        	query.addConditions(statistic.DATE_STR.lessOrEqual(dateStrEnd).or(statistic.DATE_STR.isNull()));
        }
        query.fetch().map(f -> {
        	ListBillStatisticByBuildingDTO convertDTO = convertEhPaymentBillItemsStatisticBuilding(f, statistic);
        	response[0] = ConvertHelper.convert(convertDTO, ListBillStatisticByBuildingDTO.class);
        	response[0].setOwnerId(f.getValue(statistic.OWNER_ID));
        	response[0].setOwnerType(f.getValue(statistic.OWNER_TYPE));
        	return null;
        });
        return response[0];
	}

	public List<ListBillStatisticByAddressDTO> listBillStatisticByAddress(Integer pageOffSet, Integer pageSize,
			Integer namespaceId, Long ownerId, String ownerType, String dateStrBegin, String dateStrEnd,
			String buildingName, List<String> apartmentNameList, List<Long> chargingItemIdList, String targetName) {
		List<ListBillStatisticByAddressDTO> list = new ArrayList<ListBillStatisticByAddressDTO>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhPaymentBillItems r = Tables.EH_PAYMENT_BILL_ITEMS.as("r");
        EhPaymentBills bills = Tables.EH_PAYMENT_BILLS.as("bills");
		SelectQuery<Record> query = context.selectQuery();
        //缴费信息明细表-房源维度的统计是实时统计，因为已经细到最细的维度即收费项目维度；
        query.addSelect(r.BUILDING_NAME, r.APARTMENT_NAME, r.TARGET_NAME,
        		r.AMOUNT_RECEIVABLE, r.AMOUNT_RECEIVED, r.AMOUNT_OWED,
        		r.AMOUNT_RECEIVABLE_WITHOUT_TAX, r.AMOUNT_RECEIVED_WITHOUT_TAX, r.AMOUNT_OWED_WITHOUT_TAX,
        		r.TAX_AMOUNT, r.CHARGING_ITEMS_ID, r.CATEGORY_ID,
        		bills.DUE_DAY_COUNT, bills.NOTICE_TIMES,
        		bills.NOTICETEL, bills.TARGET_NAME, bills.DATE_STR_BEGIN, bills.DATE_STR_END);
        query.addFrom(r);
		query.addJoin(bills, JoinType.LEFT_OUTER_JOIN, r.BILL_ID.eq(bills.ID));
        query.addConditions(r.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(r.OWNER_ID.eq(ownerId));
        query.addConditions(r.OWNER_TYPE.eq(ownerType));
        if(!org.springframework.util.StringUtils.isEmpty(dateStrBegin)) {
        	query.addConditions(r.DATE_STR.greaterOrEqual(dateStrBegin));
        }
        if(!org.springframework.util.StringUtils.isEmpty(dateStrEnd)) {
        	query.addConditions(r.DATE_STR.lessOrEqual(dateStrEnd));
        }
        query.addConditions(r.BUILDING_NAME.eq(buildingName));
        query.addConditions(r.APARTMENT_NAME.in(apartmentNameList));
        query.addConditions(r.CHARGING_ITEMS_ID.in(chargingItemIdList));
        if(!org.springframework.util.StringUtils.isEmpty(targetName)) {
        	query.addConditions(r.TARGET_NAME.greaterOrEqual(targetName));
        }
        query.addConditions(bills.SWITCH.eq((byte)1));//只统计已出账单的已缴和未缴费用
        query.addConditions(bills.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
        query.addOrderBy(r.BUILDING_NAME, r.APARTMENT_NAME);
        query.addLimit(pageOffSet,pageSize+1);
        query.fetch().map(f -> {
        	ListBillStatisticByAddressDTO dto = convertBillStatisticByAddress(f, r, bills);
        	dto.setAddressName(f.getValue(r.BUILDING_NAME) + "-" + f.getValue(r.APARTMENT_NAME));
        	dto.setTargetName(f.getValue(r.TARGET_NAME));
        	dto.setNoticeTel(f.getValue(bills.NOTICETEL));
        	dto.setDateStrBegin(f.getValue(bills.DATE_STR_BEGIN));
        	dto.setDateStrEnd(f.getValue(bills.DATE_STR_END));
        	Long chargingItemId = f.getValue(r.CHARGING_ITEMS_ID);
        	Long categoryId = f.getValue(r.CATEGORY_ID);;
        	String projectChargingItemName = assetProvider.getProjectChargingItemName(namespaceId, ownerId, ownerType, chargingItemId, categoryId);
            dto.setProjectChargingItemName(projectChargingItemName);
        	list.add(dto);
        	return null;
        });
      //TODO 调用资产的接口获取资产相关的统计数据
//    	Integer currentNamespaceId = f.getValue(statistic.NAMESPACE_ID);
//    	Long currentOwnerId = f.getValue(statistic.OWNER_ID);
//    	String currentOwnerType = f.getValue(statistic.OWNER_TYPE);
        return list;
	}

	private ListBillStatisticByAddressDTO convertBillStatisticByAddress(Record f, EhPaymentBillItems r, EhPaymentBills bills) {
		ListBillStatisticByAddressDTO dto = new ListBillStatisticByAddressDTO();
		BigDecimal amountReceivable = f.getValue(r.AMOUNT_RECEIVABLE);
    	BigDecimal amountReceived = f.getValue(r.AMOUNT_RECEIVED);
    	BigDecimal amountOwed = f.getValue(r.AMOUNT_OWED);
    	BigDecimal amountReceivableWithoutTax = f.getValue(r.AMOUNT_RECEIVABLE_WITHOUT_TAX);
    	BigDecimal amountReceivedWithoutTax = f.getValue(r.AMOUNT_RECEIVED_WITHOUT_TAX);
    	BigDecimal amountOwedWithoutTax = f.getValue(r.AMOUNT_OWED_WITHOUT_TAX);
    	BigDecimal taxAmount = f.getValue(r.TAX_AMOUNT);
    	Long dueDayCount = f.getValue(bills.DUE_DAY_COUNT);
    	Integer noticeTimes = f.getValue(bills.NOTICE_TIMES);
    	
    	dto.setAmountReceivable(amountReceivable != null ? amountReceivable : BigDecimal.ZERO);
    	dto.setAmountReceived(amountReceived != null ? amountReceived : BigDecimal.ZERO);
    	dto.setAmountOwed(amountOwed != null ? amountOwed : BigDecimal.ZERO);
    	dto.setAmountReceivableWithoutTax(amountReceivableWithoutTax != null ? amountReceivableWithoutTax : BigDecimal.ZERO);
    	dto.setAmountReceivedWithoutTax(amountReceivedWithoutTax != null ? amountReceivedWithoutTax : BigDecimal.ZERO);
    	dto.setAmountOwedWithoutTax(amountOwedWithoutTax != null ? amountOwedWithoutTax : BigDecimal.ZERO);
    	dto.setTaxAmount(taxAmount != null ? taxAmount : BigDecimal.ZERO);
    	dto.setDueDayCount(dueDayCount != null ? new BigDecimal(dueDayCount) : BigDecimal.ZERO);
    	dto.setNoticeTimes(noticeTimes != null ? new BigDecimal(noticeTimes) : BigDecimal.ZERO);
    	//收缴率=已收含税金额/应收含税金额  
    	BigDecimal collectionRate = calculateCollecionRate(amountReceivable, amountReceived);
    	dto.setCollectionRate(collectionRate);
		return dto;
	}

	public ListBillStatisticByAddressDTO listBillStatisticByAddressTotal(Integer namespaceId, Long ownerId,
			String ownerType, String dateStrBegin, String dateStrEnd, String buildingName,
			List<String> apartmentNameList, List<Long> chargingItemIdList, String targetName) {
		ListBillStatisticByAddressDTO dto = new ListBillStatisticByAddressDTO();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhPaymentBillItems r = Tables.EH_PAYMENT_BILL_ITEMS.as("r");
        EhPaymentBills bills = Tables.EH_PAYMENT_BILLS.as("bills");
		SelectQuery<Record> query = context.selectQuery();
        //缴费信息明细表-房源维度的统计是实时统计，因为已经细到最细的维度即收费项目维度；
        query.addSelect(r.AMOUNT_RECEIVABLE, r.AMOUNT_RECEIVED, r.AMOUNT_OWED,
        		r.AMOUNT_RECEIVABLE_WITHOUT_TAX, r.AMOUNT_RECEIVED_WITHOUT_TAX, r.AMOUNT_OWED_WITHOUT_TAX,
        		r.TAX_AMOUNT, bills.DUE_DAY_COUNT, bills.NOTICE_TIMES);
        query.addFrom(r);
		query.addJoin(bills, JoinType.LEFT_OUTER_JOIN, r.BILL_ID.eq(bills.ID));
        query.addConditions(r.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(r.OWNER_ID.eq(ownerId));
        query.addConditions(r.OWNER_TYPE.eq(ownerType));
        if(!org.springframework.util.StringUtils.isEmpty(dateStrBegin)) {
        	query.addConditions(r.DATE_STR.greaterOrEqual(dateStrBegin));
        }
        if(!org.springframework.util.StringUtils.isEmpty(dateStrEnd)) {
        	query.addConditions(r.DATE_STR.lessOrEqual(dateStrEnd));
        }
        query.addConditions(r.BUILDING_NAME.eq(buildingName));
        query.addConditions(r.APARTMENT_NAME.in(apartmentNameList));
        query.addConditions(r.CHARGING_ITEMS_ID.in(chargingItemIdList));
        if(!org.springframework.util.StringUtils.isEmpty(targetName)) {
        	query.addConditions(r.TARGET_NAME.greaterOrEqual(targetName));
        }
        query.addConditions(bills.SWITCH.eq((byte)1));//只统计已出账单的已缴和未缴费用
        query.addConditions(bills.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
        query.addOrderBy(r.BUILDING_NAME, r.APARTMENT_NAME);
        dto = convertBillStatisticByAddress(query, r, bills);
        //TODO 调用资产的接口获取资产相关的统计数据
//    	Integer currentNamespaceId = f.getValue(statistic.NAMESPACE_ID);
//    	Long currentOwnerId = f.getValue(statistic.OWNER_ID);
//    	String currentOwnerType = f.getValue(statistic.OWNER_TYPE);
        return dto;
	}
	
	private ListBillStatisticByAddressDTO convertBillStatisticByAddress(SelectQuery<Record> query, EhPaymentBillItems r, EhPaymentBills bills) {
		ListBillStatisticByAddressDTO dto = new ListBillStatisticByAddressDTO();
		final BigDecimal[] countFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountReceivableFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountReceivedFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountOwedFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountReceivableWithoutTaxFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountReceivedWithoutTaxFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountOwedWithoutTaxFinal = {BigDecimal.ZERO};
		final BigDecimal[] taxAmountFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountExemptionFinal = {BigDecimal.ZERO};
		final BigDecimal[] amountSupplementFinal = {BigDecimal.ZERO};
		final BigDecimal[] dueDayCountFinal = {BigDecimal.ZERO};
		final BigDecimal[] noticeTimesFinal = {BigDecimal.ZERO};
		query.fetch().map(f -> {
			BigDecimal amountReceivable = f.getValue(r.AMOUNT_RECEIVABLE);
	    	BigDecimal amountReceived = f.getValue(r.AMOUNT_RECEIVED);
	    	BigDecimal amountOwed = f.getValue(r.AMOUNT_OWED);
	    	BigDecimal amountReceivableWithoutTax = f.getValue(r.AMOUNT_RECEIVABLE_WITHOUT_TAX);
	    	BigDecimal amountReceivedWithoutTax = f.getValue(r.AMOUNT_RECEIVED_WITHOUT_TAX);
	    	BigDecimal amountOwedWithoutTax = f.getValue(r.AMOUNT_OWED_WITHOUT_TAX);
	    	BigDecimal taxAmount = f.getValue(r.TAX_AMOUNT);
	    	Long dueDayCount = f.getValue(bills.DUE_DAY_COUNT);
	    	Integer noticeTimes = f.getValue(bills.NOTICE_TIMES);
        	
	    	amountReceivableFinal[0] = amountReceivableFinal[0].add(amountReceivable != null ? amountReceivable : BigDecimal.ZERO);
	    	amountReceivedFinal[0] = amountReceivedFinal[0].add(amountReceived != null ? amountReceived : BigDecimal.ZERO);
	    	amountOwedFinal[0] = amountOwedFinal[0].add(amountOwed != null ? amountOwed : BigDecimal.ZERO);
	    	amountReceivableWithoutTaxFinal[0] = amountReceivableWithoutTaxFinal[0].add(amountReceivableWithoutTax != null ? amountReceivableWithoutTax : BigDecimal.ZERO);
	    	amountReceivedWithoutTaxFinal[0] = amountReceivedWithoutTaxFinal[0].add(amountReceivedWithoutTax != null ? amountReceivedWithoutTax : BigDecimal.ZERO);
	    	amountOwedWithoutTaxFinal[0] = amountOwedWithoutTaxFinal[0].add(amountOwedWithoutTax != null ? amountOwedWithoutTax : BigDecimal.ZERO);
	    	taxAmountFinal[0] = taxAmountFinal[0].add(taxAmount != null ? taxAmount : BigDecimal.ZERO);
	    	dueDayCountFinal[0] = dueDayCountFinal[0].add(dueDayCount != null ? new BigDecimal(dueDayCount) : BigDecimal.ZERO);
	    	noticeTimesFinal[0] = noticeTimesFinal[0].add(noticeTimes != null ? new BigDecimal(noticeTimes) : BigDecimal.ZERO);
        	
	    	countFinal[0] = countFinal[0].add(BigDecimal.ONE);//合计总数
	    	return null;
        });
		//收缴率=已收含税金额/应收含税金额  
    	BigDecimal collectionRate = calculateCollecionRate(amountReceivableFinal[0] , amountReceivedFinal[0]);
    	dto.setAmountReceivable(amountReceivableFinal[0] != null ? amountReceivableFinal[0] : BigDecimal.ZERO);
    	dto.setAmountReceived(amountReceivedFinal[0] != null ? amountReceivedFinal[0] : BigDecimal.ZERO);
    	dto.setAmountOwed(amountOwedFinal[0] != null ? amountOwedFinal[0] : BigDecimal.ZERO);
    	dto.setAmountReceivableWithoutTax(amountReceivableWithoutTaxFinal[0] != null ? amountReceivableWithoutTaxFinal[0] : BigDecimal.ZERO);
    	dto.setAmountReceivedWithoutTax(amountReceivedWithoutTaxFinal[0] != null ? amountReceivedWithoutTaxFinal[0] : BigDecimal.ZERO);
    	dto.setAmountOwedWithoutTax(amountOwedWithoutTaxFinal[0] != null ? amountOwedWithoutTaxFinal[0] : BigDecimal.ZERO);
    	dto.setTaxAmount(taxAmountFinal[0] != null ? taxAmountFinal[0] : BigDecimal.ZERO);
    	dto.setAmountExemption(amountExemptionFinal[0] != null ? amountExemptionFinal[0] : BigDecimal.ZERO);
    	dto.setAmountSupplement(amountSupplementFinal[0] != null ? amountSupplementFinal[0] : BigDecimal.ZERO);
    	dto.setDueDayCount(dueDayCountFinal[0] != null ? dueDayCountFinal[0] : BigDecimal.ZERO);
    	dto.setNoticeTimes(noticeTimesFinal[0] != null ? noticeTimesFinal[0] : BigDecimal.ZERO);
    	dto.setCollectionRate(collectionRate);
    	
    	dto.setCount(countFinal[0]);
		return dto;
	}
    
}