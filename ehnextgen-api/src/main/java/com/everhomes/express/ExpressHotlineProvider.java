// @formatter:off
package com.everhomes.express;

import java.util.List;

public interface ExpressHotlineProvider {

	void createExpressHotline(ExpressHotline expressHotline);

	void updateExpressHotline(ExpressHotline expressHotline);

	ExpressHotline findExpressHotlineById(Long id);

	List<ExpressHotline> listExpressHotline();

}