package com.everhomes.asset.bill;

import java.util.List;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.asset.AssetPaymentBillDeleteFlag;
import com.everhomes.rest.asset.AssetPaymentBillStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
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
                    .where(Tables.EH_PAYMENT_BILLS.ID.in(billIdList))
                    .and(Tables.EH_PAYMENT_BILLS.STATUS.notEqual(AssetPaymentBillStatus.PAID.getCode())) //已支付的不允许删除
                    .execute();
            //删除费项（置状态）
            context.update(Tables.EH_PAYMENT_BILL_ITEMS)
	    		.set(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG, AssetPaymentBillDeleteFlag.DELETE.getCode())
	            .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.in(billIdList))
	            .and(Tables.EH_PAYMENT_BILL_ITEMS.STATUS.notEqual(AssetPaymentBillStatus.PAID.getCode())) //已支付的不允许删除
	            .execute();
            return null;
        });
	}

	public Byte checkContractIsProduceBill(Integer namespaceId, String ownerType, Long ownerId, Long contractId) {
		
		
		
		
		return AssetPaymentBillStatus.PAID.getCode();
	}
 
    
    
    

}