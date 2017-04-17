// @formatter:off
package com.everhomes.express;

import java.util.List;

public interface ExpressAddressProvider {

	void createExpressAddress(ExpressAddress expressAddress);

	void updateExpressAddress(ExpressAddress expressAddress);

	ExpressAddress findExpressAddressById(Long id);

	List<ExpressAddress> listExpressAddress();

}