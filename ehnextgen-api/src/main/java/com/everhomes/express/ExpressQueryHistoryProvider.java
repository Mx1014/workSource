// @formatter:off
package com.everhomes.express;

import java.util.List;

public interface ExpressQueryHistoryProvider {

	void createExpressQueryHistory(ExpressQueryHistory expressQueryHistory);

	void updateExpressQueryHistory(ExpressQueryHistory expressQueryHistory);

	ExpressQueryHistory findExpressQueryHistoryById(Long id);

	List<ExpressQueryHistory> listExpressQueryHistory();

	List<ExpressQueryHistory> listExpressQueryHistoryByUser(Integer namespaecId, Long userId, Integer pageSize);

	void clearExpressQueryHistory(Integer namespaecId, Long userId);

	ExpressQueryHistory findExpressQueryHistory(Integer namespaceId, Long userId, Long expressCompanyId, String billNo);

}