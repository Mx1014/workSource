// @formatter:off
package com.everhomes.express;

import java.util.List;

public interface ExpressOrderLogProvider {

	void createExpressOrderLog(ExpressOrderLog expressOrderLog);

	void updateExpressOrderLog(ExpressOrderLog expressOrderLog);

	ExpressOrderLog findExpressOrderLogById(Long id);

	List<ExpressOrderLog> listExpressOrderLog();

}