// @formatter:off
package com.everhomes.express;

import java.util.List;

public interface ExpressOrderProvider {

	void createExpressOrder(ExpressOrder expressOrder);

	void updateExpressOrder(ExpressOrder expressOrder);

	ExpressOrder findExpressOrderById(Long id);

	List<ExpressOrder> listExpressOrder();

}