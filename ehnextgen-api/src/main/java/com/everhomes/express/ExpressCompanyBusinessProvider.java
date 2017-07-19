// @formatter:off
package com.everhomes.express;

import java.util.List;

public interface ExpressCompanyBusinessProvider {

	void createExpressCompanyBusiness(ExpressCompanyBusiness expressCompanyBusiness);

	void updateExpressCompanyBusiness(ExpressCompanyBusiness expressCompanyBusiness);

	ExpressCompanyBusiness findExpressCompanyBusinessById(Long id);

	List<ExpressCompanyBusiness> listExpressCompanyBusiness();

}