// @formatter:off
package com.everhomes.express;

import java.util.List;

public interface ExpressUserProvider {

	void createExpressUser(ExpressUser expressUser);

	void updateExpressUser(ExpressUser expressUser);

	ExpressUser findExpressUserById(Long id);

	List<ExpressUser> listExpressUser();

}