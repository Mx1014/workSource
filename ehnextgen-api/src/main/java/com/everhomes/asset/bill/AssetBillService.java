
package com.everhomes.asset.bill;

import com.everhomes.rest.asset.bill.BatchDeleteBillCommand;
import com.everhomes.rest.asset.bill.BatchDeleteBillFromContractCmd;
import com.everhomes.rest.asset.bill.BatchDeleteBillResponse;
import com.everhomes.rest.asset.bill.CheckContractIsProduceBillCmd;
import com.everhomes.rest.asset.bill.ListBatchDeleteBillFromContractResponse;
import com.everhomes.rest.asset.bill.ListCheckContractIsProduceBillResponse;

/**
 * @author created by ycx
 * @date 下午4:08:24
 */
public interface AssetBillService {

	BatchDeleteBillResponse batchDeleteBill(BatchDeleteBillCommand cmd);

	ListCheckContractIsProduceBillResponse checkContractIsProduceBill(CheckContractIsProduceBillCmd cmd);

	ListBatchDeleteBillFromContractResponse batchDeleteBillFromContract(BatchDeleteBillFromContractCmd cmd);
	
	
	
}
