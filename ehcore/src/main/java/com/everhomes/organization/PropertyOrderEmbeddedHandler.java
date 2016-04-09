package com.everhomes.organization;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.family.FamilyBillingAccount;
import com.everhomes.family.FamilyBillingTransactions;
import com.everhomes.family.FamilyProvider;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.organization.pm.CommunityPmBill;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.organization.AccountType;
import com.everhomes.rest.organization.BillTransactionResult;
import com.everhomes.rest.organization.OrganizationOrderStatus;
import com.everhomes.rest.organization.OrganizationOrderType;
import com.everhomes.rest.organization.PaidType;
import com.everhomes.rest.organization.TxType;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;

@Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX+OrderType.WU_YE_TEST_CODE)
public class PropertyOrderEmbeddedHandler implements OrderEmbeddedHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyOrderEmbeddedHandler.class);
	
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private CoordinationProvider coordinationProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
	private FamilyProvider familyProvider;
	
	
	@Override
	public void paySuccess(PayCallbackCommand cmd) {
		if(StringUtils.isEmpty(cmd.getPayAmount())){
			LOGGER.error("Invalid parameter,payAmount is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter,payAmount is null");
		}

		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
		OrganizationOrder order = this.checkOrder(orderId);
		BigDecimal payAmount = new BigDecimal(cmd.getPayAmount());//支付金额
		CommunityPmBill bill = this.checkPmBill(order.getBillId());
		this.checkVendorTypeFormat(cmd.getVendorType());

		Long payTime = System.currentTimeMillis();
		Timestamp payTimeStamp = new Timestamp(payTime);//支付时间

		Long cunnentTime = System.currentTimeMillis();
		Timestamp currentTimestamp = new Timestamp(cunnentTime);

		if(LOGGER.isDebugEnabled()){
			LOGGER.error("before lock.order="+order.toString());
		}
		if(order.getStatus().byteValue() == OrganizationOrderStatus.WAITING_FOR_PAY.getCode()){
			String uuidStr = UUID.randomUUID().toString();
			//pre-set-parameter-familyTx
			FamilyBillingTransactions familyTx = new FamilyBillingTransactions();
			familyTx.setOrderType(OrganizationOrderType.ORGANIZATION_ORDERS.getCode());
			familyTx.setChargeAmount(payAmount.negate());
			familyTx.setCreateTime(payTimeStamp);
			familyTx.setOperatorUid(0L);
			familyTx.setOwnerId(bill.getEntityId());
			familyTx.setPaidType(PaidType.SELFPAY.getCode());
			familyTx.setResultCodeId(BillTransactionResult.SUCCESS.getCode());
			//familyTx.setResultCodeScope("test");
			familyTx.setResultDesc(BillTransactionResult.SUCCESS.getDescription());
			familyTx.setTargetAccountType(AccountType.ORGANIZATION.getCode());
			familyTx.setTxSequence(uuidStr);
			familyTx.setTxType(TxType.ONLINE.getCode());
			familyTx.setVendor(cmd.getVendorType());
			familyTx.setPayAccount(cmd.getPayAccount());
			//pre-set-parameter-orgTx
			OrganizationBillingTransactions orgTx = new OrganizationBillingTransactions();
			orgTx.setOrderType(OrganizationOrderType.ORGANIZATION_ORDERS.getCode());
			orgTx.setChargeAmount(payAmount);
			orgTx.setCreateTime(payTimeStamp);
			orgTx.setOperatorUid(0L);
			orgTx.setOwnerId(bill.getOrganizationId());
			orgTx.setPaidType(PaidType.SELFPAY.getCode());
			orgTx.setResultCodeId(BillTransactionResult.SUCCESS.getCode());
			//orgTx.setResultCodeScope("test");
			orgTx.setResultDesc(BillTransactionResult.SUCCESS.getDescription());
			orgTx.setTargetAccountType(AccountType.FAMILY.getCode());
			orgTx.setTxSequence(uuidStr);
			orgTx.setTxType(TxType.ONLINE.getCode());
			orgTx.setVendor(cmd.getVendorType());
			orgTx.setPayAccount(cmd.getPayAccount());

			Tuple<OrganizationOrder, Boolean> result = this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_PM_ORDER.getCode()).enter(() -> {
				long lqStartTime = System.currentTimeMillis();
				OrganizationOrder order2 = this.organizationProvider.findOrganizationOrderById(orderId);
				long lqEndTime = System.currentTimeMillis();
				LOGGER.error("find pm order in the lock.elapse="+(lqEndTime-lqStartTime));

				if(LOGGER.isDebugEnabled()){
					LOGGER.error("in lock.order="+order2.toString());
				}

				if(order2.getStatus().byteValue() == OrganizationOrderStatus.WAITING_FOR_PAY.getCode()){
					long luStartTime = System.currentTimeMillis();
					this.dbProvider.execute(s -> {
						order2.setAmount(payAmount);
						this.updateOrderStatus(order2,0L,payTimeStamp,OrganizationOrderStatus.PAID.getCode());

						FamilyBillingAccount fAccount = this.findOrNewFBillAcountNoLock(bill.getEntityId(),currentTimestamp);
						OrganizationBillingAccount oAccount = this.findOrNewOBillAccountNoLock(bill.getOrganizationId(),currentTimestamp);

						familyTx.setOrderId(order2.getId());
						familyTx.setOwnerAccountId(fAccount.getId());
						familyTx.setTargetAccountId(oAccount.getId());
						this.familyProvider.createFamilyBillingTransaction(familyTx);

						orgTx.setOrderId(order2.getId());
						orgTx.setOwnerAccountId(oAccount.getId());
						orgTx.setTargetAccountId(fAccount.getId());
						this.organizationProvider.createOrganizationBillingTransaction(orgTx);

						if(LOGGER.isDebugEnabled()){
							LOGGER.error("check online pm pay amount.oAccountAmount="+oAccount.getBalance()+".payAmount="+payAmount+".order="+order2.toString());
						}

						//线上支付,将金额存到物业账号中
						oAccount.setBalance(oAccount.getBalance().add(payAmount));
						oAccount.setUpdateTime(currentTimestamp);
						this.organizationProvider.updateOrganizationBillingAccount(oAccount);

						return true;
					});
					long luEndTime = System.currentTimeMillis();
					LOGGER.error("update pm order in the lock.elapse="+(luEndTime-luStartTime));
				}
				return order2;
			});

			if(LOGGER.isDebugEnabled()){
				LOGGER.error("pm order lock finish.status="+result.second());
			}
		}
		
	}

	@Override
	public void payFail(PayCallbackCommand cmd) {
		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
		OrganizationOrder order = this.checkOrder(orderId);
		Date cunnentTime = new Date();
		Timestamp currentTimestamp = new Timestamp(cunnentTime.getTime());
		this.updateOrderStatus(order,0L,currentTimestamp,OrganizationOrderStatus.INACTIVE.getCode());
		
	}
	
	private OrganizationOrder checkOrder(Long orderId) {
		OrganizationOrder order = this.organizationProvider.findOrganizationOrderById(orderId);
		if(order == null){
			LOGGER.error("the order not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the order not found.");
		}
		return order;
	}

	private Long convertOrderNoToOrderId(String orderNo) {
		return Long.valueOf(orderNo);
	}
	
	private void updateOrderStatus(OrganizationOrder order, long payerId,Timestamp payTimeStamp, byte code) {
		order.setPayerId(0L);
		order.setPaidTime(payTimeStamp);
		order.setStatus(code);
		this.organizationProvider.updateOrganizationOrder(order);
	}
	
	private CommunityPmBill checkPmBill(Long billId) {
		CommunityPmBill bill = this.organizationProvider.findOranizationBillById(billId);
		if(bill == null){
			LOGGER.error("the bill not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the bill not found.");
		}
		return bill;
	}
	
	private void checkVendorTypeFormat(String vendorType) {
		if(VendorType.fromCode(vendorType) == null){
			LOGGER.error("vendor type is wrong.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"vendor type is wrong.");
		}
	}
	
	private FamilyBillingAccount findOrNewFBillAcountNoLock(Long entityId,Timestamp currentTimestamp) {
		FamilyBillingAccount fAccount = this.familyProvider.findFamilyBillingAccountByOwnerId(entityId);
		if(fAccount == null){
			fAccount = new FamilyBillingAccount();
			fAccount.setAccountNumber(BillingAccountHelper.getAccountNumberByBillingAccountTypeCode(BillingAccountType.FAMILY.getCode()));
			fAccount.setBalance(BigDecimal.ZERO);
			fAccount.setCreateTime(currentTimestamp);
			fAccount.setOwnerId(entityId);
			this.familyProvider.createFamilyBillingAccount(fAccount);
		}
		return fAccount;
	}
	
	private OrganizationBillingAccount findOrNewOBillAccountNoLock(Long orgId,Timestamp currentTimestamp) {
		OrganizationBillingAccount oAccount = this.organizationProvider.findOrganizationBillingAccount(orgId);
		if(oAccount == null){
			oAccount = new OrganizationBillingAccount();
			oAccount.setAccountNumber(BillingAccountHelper.getAccountNumberByBillingAccountTypeCode(BillingAccountType.ORGANIZATION.getCode()));
			oAccount.setBalance(BigDecimal.ZERO);
			oAccount.setCreateTime(currentTimestamp);
			oAccount.setOwnerId(orgId);
			this.organizationProvider.createOrganizationBillingAccount(oAccount);
		}
		return oAccount;
	}
	

}
