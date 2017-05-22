// @formatter:off
package com.everhomes.express;

import java.util.List;

import com.everhomes.rest.express.ExpressOwner;
import com.everhomes.rest.express.ExpressOwnerType;

public interface ExpressAddressProvider {

	void createExpressAddress(ExpressAddress expressAddress);

	void updateExpressAddress(ExpressAddress expressAddress);

	ExpressAddress findExpressAddressById(Long id);

	List<ExpressAddress> listExpressAddress();

	void updateOtherAddressToNotDefault(Integer namespaceId, ExpressOwnerType ownerType, Long ownerId, Long userId,
			Long currentId, Byte category);

	List<ExpressAddress> listExpressAddressByOwner(ExpressOwner owner, Byte category);

	ExpressAddress findAnyExpressAddressByOwner(ExpressOwner owner, Byte category);

	ExpressAddress findAnyExpressAddressByOwner(ExpressOwner owner, Byte category, Long currentId);

}