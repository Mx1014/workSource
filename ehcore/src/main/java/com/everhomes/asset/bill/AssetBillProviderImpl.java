package com.everhomes.asset.bill;

import java.util.List;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.asset.PaymentBills;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.asset.AssetPaymentBillDeleteFlag;
import com.everhomes.rest.asset.AssetPaymentBillStatus;
import com.everhomes.rest.asset.statistic.BuildingStatisticParam;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPaymentBillItems;
import com.everhomes.server.schema.tables.EhPaymentBills;
/**
 * @author created by ycx
 * @date 下午4:06:16
 */
@Component
public class AssetBillProviderImpl implements AssetBillProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetBillProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;

	public void batchDeleteBill(Integer namespaceId, String ownerType, Long ownerId, List<Long> billIdList) {
		this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            //删除账单（置状态）
            context.update(Tables.EH_PAYMENT_BILLS)
            		.set(Tables.EH_PAYMENT_BILLS.DELETE_FLAG, AssetPaymentBillDeleteFlag.DELETE.getCode())
                    .where(Tables.EH_PAYMENT_BILLS.NAMESPACE_ID.eq(namespaceId))
                    .and(Tables.EH_PAYMENT_BILLS.OWNER_TYPE.eq(ownerType))
                    .and(Tables.EH_PAYMENT_BILLS.OWNER_ID.eq(ownerId))
                    .and(Tables.EH_PAYMENT_BILLS.STATUS.notEqual(AssetPaymentBillStatus.PAID.getCode())) //已支付的不允许删除
                    .and(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))
                    .and(Tables.EH_PAYMENT_BILLS.ID.in(billIdList))
                    .execute();
            //删除费项（置状态）
            context.update(Tables.EH_PAYMENT_BILL_ITEMS)
	    		.set(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG, AssetPaymentBillDeleteFlag.DELETE.getCode())
	    		.where(Tables.EH_PAYMENT_BILL_ITEMS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_PAYMENT_BILL_ITEMS.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_PAYMENT_BILL_ITEMS.OWNER_ID.eq(ownerId))
                .and(Tables.EH_PAYMENT_BILL_ITEMS.STATUS.notEqual(AssetPaymentBillStatus.PAID.getCode())) //已支付的不允许删除
                .and(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))
                .and(Tables.EH_PAYMENT_BILL_ITEMS.ID.in(billIdList))
	            .execute();
            return null;
        });
	}

	public Byte checkContractIsProduceBill(Integer namespaceId, String ownerType, Long ownerId, Long contractId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhPaymentBills r = Tables.EH_PAYMENT_BILLS.as("r");
        List<PaymentBills> list = context.selectFrom(r)
				.where(r.NAMESPACE_ID.eq(namespaceId))
				.and(r.OWNER_TYPE.eq(ownerType))
				.and(r.OWNER_ID.eq(ownerId))
				.and(r.CONTRACT_ID.eq(contractId))
				.and(r.STATUS.eq(AssetPaymentBillStatus.PAID.getCode()))
				.and(r.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))
				.fetchInto(PaymentBills.class);
        if(list != null && list.size() > 0) {
        	return AssetPaymentBillStatus.PAID.getCode();
        }else {
        	return AssetPaymentBillStatus.UNPAID.getCode();
        }
	}

	public void deleteBillFromContract(Integer namespaceId, String ownerType, Long ownerId, Long contractId) {
		this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            //删除账单（置状态）
            context.update(Tables.EH_PAYMENT_BILLS)
            		.set(Tables.EH_PAYMENT_BILLS.DELETE_FLAG, AssetPaymentBillDeleteFlag.DELETE.getCode())
                    .where(Tables.EH_PAYMENT_BILLS.NAMESPACE_ID.eq(namespaceId))
                    .and(Tables.EH_PAYMENT_BILLS.OWNER_TYPE.eq(ownerType))
                    .and(Tables.EH_PAYMENT_BILLS.OWNER_ID.eq(ownerId))
                    .and(Tables.EH_PAYMENT_BILLS.STATUS.notEqual(AssetPaymentBillStatus.PAID.getCode())) //已支付的不允许删除
                    .and(Tables.EH_PAYMENT_BILLS.CONTRACT_ID.eq(contractId))
                    .execute();
            //删除费项（置状态）
            context.update(Tables.EH_PAYMENT_BILL_ITEMS)
	    		.set(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG, AssetPaymentBillDeleteFlag.DELETE.getCode())
	    		.where(Tables.EH_PAYMENT_BILL_ITEMS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_PAYMENT_BILL_ITEMS.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_PAYMENT_BILL_ITEMS.OWNER_ID.eq(ownerId))
                .and(Tables.EH_PAYMENT_BILL_ITEMS.STATUS.notEqual(AssetPaymentBillStatus.PAID.getCode())) //已支付的不允许删除
                .and(Tables.EH_PAYMENT_BILL_ITEMS.CONTRACT_ID.eq(contractId))
	            .execute();
            return null;
        });
	}
    

}