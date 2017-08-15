// @formatter:off
package com.everhomes.express;

import java.util.List;

import com.everhomes.rest.express.ExpressOwner;

public interface ExpressCompanyProvider {

	void createExpressCompany(ExpressCompany expressCompany);

	void updateExpressCompany(ExpressCompany expressCompany);

	ExpressCompany findExpressCompanyById(Long id);

	List<ExpressCompany> listExpressCompany();

	List<ExpressCompany> listExpressCompanyByOwner(ExpressOwner owner);

	ExpressCompany findExpressCompanyByAppKeyAndAuth(String appKey, String authorization);

}