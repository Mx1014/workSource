// @formatter:off
package com.everhomes.express;

import java.util.List;

public interface ExpressServiceAddresseProvider {

	void createExpressServiceAddresse(ExpressServiceAddresse expressServiceAddresse);

	void updateExpressServiceAddresse(ExpressServiceAddresse expressServiceAddresse);

	ExpressServiceAddresse findExpressServiceAddresseById(Long id);

	List<ExpressServiceAddresse> listExpressServiceAddresse();

}