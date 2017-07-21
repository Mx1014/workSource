// @formatter:off
package com.everhomes.express;

import java.util.List;

import com.everhomes.rest.express.ExpressOwner;

public interface ExpressHotlineProvider {

	void createExpressHotline(ExpressHotline expressHotline);

	void updateExpressHotline(ExpressHotline expressHotline);

	ExpressHotline findExpressHotlineById(Long id);

	List<ExpressHotline> listExpressHotline();

	List<ExpressHotline> listHotLinesByOwner(ExpressOwner owner, int pageSize, Long pageAnchor);

}