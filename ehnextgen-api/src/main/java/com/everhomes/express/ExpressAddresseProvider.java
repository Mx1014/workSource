// @formatter:off
package com.everhomes.express;

import java.util.List;

public interface ExpressAddresseProvider {

	void createExpressAddresse(ExpressAddresse expressAddresse);

	void updateExpressAddresse(ExpressAddresse expressAddresse);

	ExpressAddresse findExpressAddresseById(Long id);

	List<ExpressAddresse> listExpressAddresse();

}