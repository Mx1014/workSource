package com.everhomes.asset.bill;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.asset.PaymentBills;
import com.everhomes.asset.PaymentLateFine;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.asset.AssetItemFineType;
import com.everhomes.rest.asset.AssetPaymentBillDeleteFlag;
import com.everhomes.rest.asset.AssetPaymentBillStatus;
import com.everhomes.rest.asset.AssetSubtractionType;
import com.everhomes.rest.asset.BillGroupDTO;
import com.everhomes.rest.asset.BillItemDTO;
import com.everhomes.rest.asset.ListBillDetailResponse;
import com.everhomes.rest.asset.bill.ChangeChargeStatusCommand;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.asset.statistic.BuildingStatisticParam;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhAddresses;
import com.everhomes.server.schema.tables.EhPaymentBillItems;
import com.everhomes.server.schema.tables.EhPaymentBills;
import com.everhomes.server.schema.tables.EhPaymentChargingItems;
import com.everhomes.server.schema.tables.EhPaymentLateFine;
import com.everhomes.util.ConvertHelper;
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
                .and(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))
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
				//缺陷 #43009 新建的合同，在进行初始化的时候，获取不到新建的合同，该合同未缴费，查询账单，出现一条金额为0 的账单记录，显示已缴，导致不能初始化合同
				//如果账单是已缴，但是已收金额是0，说明是系统置为已缴状态的
				.and(r.AMOUNT_RECEIVED.ne(BigDecimal.ZERO)) 
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
            //删除账单：缺陷 #42544【合同管理V4.0】一键初始化合同后，查看修改初始化为“草稿合同”的数据，原来的费用清单依然存在
            //产品已确认，合同初始化，账单进行物理删除，不可恢复。
            context.delete(Tables.EH_PAYMENT_BILLS)
                    .where(Tables.EH_PAYMENT_BILLS.NAMESPACE_ID.eq(namespaceId))
                    .and(Tables.EH_PAYMENT_BILLS.OWNER_TYPE.eq(ownerType))
                    .and(Tables.EH_PAYMENT_BILLS.OWNER_ID.eq(ownerId))
                    .and(Tables.EH_PAYMENT_BILLS.STATUS.notEqual(AssetPaymentBillStatus.PAID.getCode())) //已支付的不允许删除
                    .and(Tables.EH_PAYMENT_BILLS.CONTRACT_ID.eq(contractId))
                    .execute();
            //删除费项：缺陷 #42544【合同管理V4.0】一键初始化合同后，查看修改初始化为“草稿合同”的数据，原来的费用清单依然存在
            //产品已确认，合同初始化，账单进行物理删除，不可恢复。
            context.delete(Tables.EH_PAYMENT_BILL_ITEMS)
	    		.where(Tables.EH_PAYMENT_BILL_ITEMS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_PAYMENT_BILL_ITEMS.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_PAYMENT_BILL_ITEMS.OWNER_ID.eq(ownerId))
                .and(Tables.EH_PAYMENT_BILL_ITEMS.STATUS.notEqual(AssetPaymentBillStatus.PAID.getCode())) //已支付的不允许删除
                .and(Tables.EH_PAYMENT_BILL_ITEMS.CONTRACT_ID.eq(contractId))
	            .execute();
            return null;
        });
	}
	
	/**
	 * 缴费V7.3(账单组规则定义)：批量删除“非已缴”账单接口
	 * 批量删除只支持删除只支持：手动新增 、 批量导入 、合同产生的账单
	 * 第三方或对接其他模块不管是已缴还是未缴都不允许删除
	 */
	public List<PaymentBills> findCannotDeleteBill(List<Long> billIdList) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhPaymentBills r = Tables.EH_PAYMENT_BILLS.as("r");
		List<PaymentBills> bills = context.selectFrom(r)
                .where(r.ID.in(billIdList))
                .and(r.STATUS.eq(AssetPaymentBillStatus.PAID.getCode())
                		.or(r.SOURCE_TYPE.ne(AssetSourceType.ASSET_MODULE)
                                .and(r.SOURCE_TYPE.ne(AssetSourceType.CONTRACT_MODULE))))
                .fetchInto(PaymentBills.class);
		return bills;
	}
    
	public ListBillDetailResponse listOpenBillDetail(Long billId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems o = Tables.EH_PAYMENT_BILL_ITEMS.as("o");
        EhPaymentChargingItems k = Tables.EH_PAYMENT_CHARGING_ITEMS.as("k");
        EhPaymentLateFine fine = Tables.EH_PAYMENT_LATE_FINE.as("fine");
        EhAddresses t1 = Tables.EH_ADDRESSES.as("t1");
        ListBillDetailResponse vo = new ListBillDetailResponse();
        BillGroupDTO dto = new BillGroupDTO();
        List<BillItemDTO> list1 = new ArrayList<>();
        context.select(o.CHARGING_ITEM_NAME,o.ID,o.AMOUNT_RECEIVABLE,t1.APARTMENT_NAME,t1.BUILDING_NAME, o.APARTMENT_NAME, o.BUILDING_NAME, o.CHARGING_ITEMS_ID
        		, o.ENERGY_CONSUME,o.AMOUNT_RECEIVABLE_WITHOUT_TAX,o.TAX_AMOUNT,o.TAX_RATE
        		, o.SOURCE_ID, o.SOURCE_TYPE, o.SOURCE_NAME, o.CONSUME_USER_ID, o.CAN_DELETE, o.CAN_MODIFY
        		, o.GOODS_SERVE_APPLY_NAME, o.DELETE_FLAG)
                .from(o)
                .leftOuterJoin(k)
                .on(o.CHARGING_ITEMS_ID.eq(k.ID))
                .leftOuterJoin(t1)
                .on(o.ADDRESS_ID.eq(t1.ID))
                .where(o.BILL_ID.eq(billId))
                //.and(o.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                .orderBy(k.DEFAULT_ORDER)
                .fetch()
                .map(f -> {
                    BillItemDTO itemDTO = new BillItemDTO();
                    itemDTO.setBillId(billId);
                    itemDTO.setBillItemName(f.getValue(o.CHARGING_ITEM_NAME));
                    itemDTO.setBillItemId(f.getValue(o.ID));
                    itemDTO.setAmountReceivable(f.getValue(o.AMOUNT_RECEIVABLE));
                    String apartFromAddr = f.getValue(t1.APARTMENT_NAME);
                    String buildingFromAddr = f.getValue(t1.BUILDING_NAME);
                    if(!org.jooq.tools.StringUtils.isBlank(apartFromAddr) || !org.jooq.tools.StringUtils.isBlank(buildingFromAddr)){
                        itemDTO.setApartmentName(apartFromAddr);
                        itemDTO.setBuildingName(buildingFromAddr);
                    }else{
                        itemDTO.setApartmentName(f.getValue(o.APARTMENT_NAME));
                        itemDTO.setBuildingName(f.getValue(o.BUILDING_NAME));
                    }
                    itemDTO.setChargingItemsId(f.getValue(o.CHARGING_ITEMS_ID));
                    itemDTO.setEnergyConsume(f.getValue(o.ENERGY_CONSUME));//费项增加用量字段
                    itemDTO.setItemFineType(AssetItemFineType.item.getCode());//增加费项类型字段
                    itemDTO.setItemType(AssetSubtractionType.item.getCode());//增加费项类型字段
                    itemDTO.setAmountReceivableWithoutTax(f.getValue(o.AMOUNT_RECEIVABLE_WITHOUT_TAX));//增加应收（不含税）
                    itemDTO.setTaxAmount(f.getValue(o.TAX_AMOUNT));//税额
                    itemDTO.setTaxRate(f.getValue(o.TAX_RATE));//税率
                    //新增账单来源信息
                    itemDTO.setSourceId(f.getValue(o.SOURCE_ID));
                    itemDTO.setSourceType(f.getValue(o.SOURCE_TYPE));
                    itemDTO.setSourceName(f.getValue(o.SOURCE_NAME));
                    itemDTO.setConsumeUserId(f.getValue(o.CONSUME_USER_ID));
                    //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
                    itemDTO.setCanDelete(f.getValue(o.CAN_DELETE));
                    itemDTO.setCanModify(f.getValue(o.CAN_MODIFY));
                    //物业缴费V7.1（企业记账流程打通）: 增加商品信息字段
                    itemDTO.setGoodsServeApplyName(f.getValue(o.GOODS_SERVE_APPLY_NAME));
                    itemDTO.setDeleteFlag(f.getValue(o.DELETE_FLAG));
                    list1.add(itemDTO);
                    return null;
                });
        // 滞纳金
        List<BillItemDTO> fineList = new ArrayList<>();
        for(BillItemDTO item : list1){
            List<PaymentLateFine> fines = context.selectFrom(fine)
                .where(fine.BILL_ITEM_ID.eq(item.getBillItemId()))
                    .fetchInto(PaymentLateFine.class);
            for(PaymentLateFine n : fines){
                BillItemDTO nitem = ConvertHelper.convert(item, BillItemDTO.class);
                // 左邻convert为浅拷贝，第一层字段更改不会影响之前的
                nitem.setBillItemName(n.getName());
                nitem.setAmountReceivable(n.getAmount());
                nitem.setAmountReceivableWithoutTax(n.getAmount());
                nitem.setTaxAmount(BigDecimal.ZERO);
                nitem.setTaxRate(BigDecimal.ZERO);
                nitem.setItemFineType(AssetItemFineType.lateFine.getCode());//增加费项类型字段
                nitem.setItemType(AssetSubtractionType.lateFine.getCode());//费项类型
                //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
                //滞纳金是当成一个费项来展示的，这个会涉及滞纳金的后台计算，应该再区分滞纳金/费项，是滞纳金不允许编辑/删除。
                nitem.setCanDelete((byte)0);
                nitem.setCanModify((byte)0);
                fineList.add(nitem);
            }
        }
        list1.addAll(fineList);
        
        dto.setBillItemDTOList(list1);
        vo.setBillGroupDTO(dto);
        return vo;
    }
	
	public void changeChargeStatus(Integer currentNamespaceId, Long billId, BigDecimal amountReceived,
			BigDecimal amountOwed, Integer paymentType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        context.update(t)
                .set(t.PAYMENT_TYPE, paymentType)
                .set(t.AMOUNT_RECEIVED, amountReceived)
                .set(t.AMOUNT_OWED, amountOwed)
                .set(t.THIRD_PAID, AssetPaymentBillStatus.PAID.getCode()) //总体原则：不支持同一笔账单即在左邻支付一半，又在EAS支付一半，不允许两边分别支付
                .where(t.ID.eq(billId))
                .and(t.NAMESPACE_ID.eq(currentNamespaceId))
                .execute();
	}


}