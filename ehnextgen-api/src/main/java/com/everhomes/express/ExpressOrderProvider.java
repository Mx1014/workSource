// @formatter:off
package com.everhomes.express;

import java.util.List;

import com.everhomes.rest.express.ListExpressOrderCondition;

public interface ExpressOrderProvider {

	void createExpressOrder(ExpressOrder expressOrder);

	void updateExpressOrder(ExpressOrder expressOrder);
	
	ExpressOrder findExpressOrderById(Long id);

	List<ExpressOrder> listExpressOrder();

	List<ExpressOrder> listExpressOrderByCondition(ListExpressOrderCondition condition);

}