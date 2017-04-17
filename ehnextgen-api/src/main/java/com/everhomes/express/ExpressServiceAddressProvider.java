// @formatter:off
package com.everhomes.express;

import java.util.List;

import com.everhomes.rest.express.ExpressOwner;

public interface ExpressServiceAddressProvider {

	void createExpressServiceAddress(ExpressServiceAddress expressServiceAddress);

	void updateExpressServiceAddress(ExpressServiceAddress expressServiceAddress);

	ExpressServiceAddress findExpressServiceAddressById(Long id);

	List<ExpressServiceAddress> listExpressServiceAddress();

	List<ExpressServiceAddress> listExpressServiceAddresseByOwner(ExpressOwner owner);

}