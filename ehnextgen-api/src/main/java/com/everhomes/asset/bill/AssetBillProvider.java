package com.everhomes.asset.bill;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.rest.asset.ListBillDetailResponse;

/**
 * @author created by ycx
 * @date 下午4:08:09
 */
public interface AssetBillProvider {

	void batchDeleteBill(Integer namespaceId, String ownerType, Long ownerId, List<Long> billIdList);

	Byte checkContractIsProduceBill(Integer namespaceId, String ownerType, Long ownerId, Long contractId);

	void deleteBillFromContract(Integer namespaceId, String ownerType, Long ownerId, Long contractId);

	ListBillDetailResponse listOpenBillDetail(Long billId);

	void changeChargeStatus(Integer currentNamespaceId, Long billId, BigDecimal amountReceived, BigDecimal amountOwed,
			Integer paymentType);
	
}
