
package com.everhomes.asset.bill;

import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.bill.BatchDeleteBillCommand;
import com.everhomes.rest.asset.bill.BatchDeleteBillFromContractCmd;
import com.everhomes.rest.asset.bill.ChangeChargeStatusCommand;
import com.everhomes.rest.asset.bill.CheckContractIsProduceBillCmd;
import com.everhomes.rest.asset.bill.ListBatchDeleteBillFromContractResponse;
import com.everhomes.rest.asset.bill.ListBillsDTO;
import com.everhomes.rest.asset.bill.ListBillsResponse;
import com.everhomes.rest.asset.bill.ListCheckContractIsProduceBillResponse;

/**
 * @author created by ycx
 * @date 下午4:08:24
 */
public interface AssetBillService {

	String batchDeleteBill(BatchDeleteBillCommand cmd);

	ListCheckContractIsProduceBillResponse checkContractIsProduceBill(CheckContractIsProduceBillCmd cmd);

	ListBatchDeleteBillFromContractResponse batchDeleteBillFromContract(BatchDeleteBillFromContractCmd cmd);

	/**
	 * 物业缴费V7.5（中天-资管与财务EAS系统对接）：查看账单列表（只传租赁账单） 
	 */
	ListBillsResponse listOpenBills(ListBillsCommand cmd);

	ListBillsDTO changeChargeStatus(ChangeChargeStatusCommand cmd);
	
}
