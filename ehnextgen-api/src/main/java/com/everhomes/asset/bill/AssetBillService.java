
package com.everhomes.asset.bill;

import com.everhomes.rest.asset.bill.BatchDeleteBillCommand;

/**
 * @author created by ycx
 * @date 下午4:08:24
 */
public interface AssetBillService {

	String batchDeleteBill(BatchDeleteBillCommand cmd);
	
	
	
}
