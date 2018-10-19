package com.everhomes.asset.statistic;

import java.util.ArrayList;
import java.util.List;

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
import com.everhomes.rest.asset.BillStaticsDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPaymentBills;
import com.everhomes.server.schema.tables.daos.EhPaymentBillItemsDao;
import com.everhomes.server.schema.tables.daos.EhPaymentBillStatisticCommunityDao;
import com.everhomes.server.schema.tables.pojos.EhPaymentBillStatisticCommunity;
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
 
    private DSLContext getReadOnlyContext(){
        return this.dbProvider.getDslContext(AccessSpec.readOnly());
    }
    
    private DSLContext getReadWriteContext(){
        return this.dbProvider.getDslContext(AccessSpec.readWrite());
    }
    
    @SuppressWarnings("rawtypes")
	private Long getNextSequence(Class clz){
        return this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(clz));
    }

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
        	long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillStatisticCommunity.class));
        	dto.setId(nextSequence);
        	dto.setAmountReceivable(f.getValue(DSL.sum(r.AMOUNT_RECEIVABLE)));
        	dto.setAmountReceived(f.getValue(DSL.sum(r.AMOUNT_RECEIVED)));
        	dto.setAmountOwed(f.getValue(DSL.sum(r.AMOUNT_OWED)));
        	dto.setAmountReceivableWithoutTax(f.getValue(DSL.sum(r.AMOUNT_RECEIVABLE_WITHOUT_TAX)));
        	dto.setAmountReceivedWithoutTax(f.getValue(DSL.sum(r.AMOUNT_RECEIVED_WITHOUT_TAX)));
        	dto.setAmountOwedWithoutTax(f.getValue(DSL.sum(r.AMOUNT_OWED_WITHOUT_TAX)));
        	dto.setTaxAmount(f.getValue(DSL.sum(r.TAX_AMOUNT)));
        	dto.setAmountExemption(f.getValue(DSL.sum(r.AMOUNT_EXEMPTION)));
        	dto.setAmountSupplement(f.getValue(DSL.sum(r.AMOUNT_SUPPLEMENT)));
        	dto.setDueDayCount(f.getValue(DSL.sum(r.DUE_DAY_COUNT)));
        	dto.setNoticeTimes(f.getValue(DSL.sum(r.NOTICE_TIMES)));
        	
        	//收缴率=已收含税金额/应收含税金额
            return null;
        });
		
	}
    
    
    
    
    
}