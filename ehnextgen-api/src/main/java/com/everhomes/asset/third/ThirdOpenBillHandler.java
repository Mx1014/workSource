package com.everhomes.asset.third;

import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.bill.ListBillsResponse;

/**
 * @author created by ycx
 * @date 2018年11月12日 下午7:59:47
 */
public interface ThirdOpenBillHandler {
    static final String THIRDOPENBILL_PREFIX = "ThirdOpenBill-";
    
    /**
     * 创建或更新缴费模块与其他模块的映射关系
     * @param assetInstanceConfigDTO
     */
    default ListBillsResponse listOpenBills(ListBillsCommand cmd) {
    	return null;
    }
    
}
