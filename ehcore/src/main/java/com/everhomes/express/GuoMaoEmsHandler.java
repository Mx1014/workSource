// @formatter:off
package com.everhomes.express;

import org.springframework.stereotype.Component;

import com.everhomes.rest.express.GetExpressLogisticsDetailResponse;
//后面的1为表eh_express_companies中父id为0的行的id
@Component(ExpressHandler.EXPRESS_HANDLER_PREFIX+"3")
public class GuoMaoEmsHandler implements ExpressHandler{

	@Override
	public String getBillNo(ExpressOrder expressOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetExpressLogisticsDetailResponse getExpressLogisticsDetail(ExpressCompany expressCompany, String billNo) {
		// TODO Auto-generated method stub
		return null;
	}

}
