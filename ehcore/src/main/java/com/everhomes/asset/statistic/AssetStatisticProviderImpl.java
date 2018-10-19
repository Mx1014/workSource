package com.everhomes.asset.statistic;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.asset.AssetPaymentBillDeleteFlag;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPaymentBills;
import com.everhomes.server.schema.tables.daos.EhPaymentBillStatisticCommunityDao;
import com.everhomes.server.schema.tables.pojos.EhPaymentBillStatisticCommunity;
import com.everhomes.util.DateHelper;
/**
 * @author created by ycx
 * @date 下午3:52:18
 */
@Component
public class AssetStatisticProviderImpl implements AssetStatisticProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetStatisticProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
 
	public void createStatisticByCommnunity(Integer namespaceId, Long ownerId, String ownerType, String dateStr) {
		//1、根据namespaceId、ownerId、ownerType、dateStr统计账单相关数据
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhPaymentBillStatisticCommunityDao statisticCommunityDao = new EhPaymentBillStatisticCommunityDao(context.configuration());
		EhPaymentBillStatisticCommunity dto = new EhPaymentBillStatisticCommunity();
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
        	
        	long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillStatisticCommunity.class));
        	dto.setId(nextSequence);
        	dto.setNamespaceId(namespaceId);
        	dto.setOwnerId(ownerId);
        	dto.setOwnerType(ownerType);
        	dto.setDateStr(dateStr);
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
        	BigDecimal collectionRate = BigDecimal.ZERO;
        	if(amountReceived != null && amountReceivable != null) {
        		collectionRate = amountReceived.divide(amountReceivable);
        	}
        	dto.setCollectionRate(collectionRate);
        	
        	dto.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        	dto.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        	statisticCommunityDao.insert(dto);
            return null;
        });
	}
    
    
    
    
    
}