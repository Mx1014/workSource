// @formatter:off
package com.everhomes.express;

import com.everhomes.rest.express.GetExpressLogisticsDetailResponse;

public interface ExpressHandler {
	
	String EXPRESS_HANDLER_PREFIX = "express_handler_";

	String getBillNo(ExpressOrder expressOrder);

	GetExpressLogisticsDetailResponse getExpressLogisticsDetail(ExpressCompany expressCompany, String billNo);
	
}
