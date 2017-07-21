// @formatter:off
package com.everhomes.express;

import java.util.List;

import com.everhomes.rest.express.ExpressOwner;

public interface ExpressCompanyBusinessProvider {

	void createExpressCompanyBusiness(ExpressCompanyBusiness expressCompanyBusiness);

	void updateExpressCompanyBusiness(ExpressCompanyBusiness expressCompanyBusiness);

	ExpressCompanyBusiness findExpressCompanyBusinessById(Long id);

	List<ExpressCompanyBusiness> listExpressCompanyBusiness();

	List<ExpressCompanyBusiness> listExpressSendTypesByOwner(int namespaceId, String ownerType, Long ownerId,
			Long expressCompanyId);

	ExpressCompanyBusiness getExpressCompanyBusinessByOwner(int namespaceId, String ownerType, Long ownerId,
			Byte sendType);

}