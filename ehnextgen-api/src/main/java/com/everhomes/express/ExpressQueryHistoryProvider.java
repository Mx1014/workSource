// @formatter:off
package com.everhomes.express;

import java.util.List;

public interface ExpressQueryHistoryProvider {

	void createExpressQueryHistory(ExpressQueryHistory expressQueryHistory);

	void updateExpressQueryHistory(ExpressQueryHistory expressQueryHistory);

	ExpressQueryHistory findExpressQueryHistoryById(Long id);

	List<ExpressQueryHistory> listExpressQueryHistory();

}