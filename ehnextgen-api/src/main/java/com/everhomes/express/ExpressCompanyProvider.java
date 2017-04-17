// @formatter:off
package com.everhomes.express;

import java.util.List;

public interface ExpressCompanyProvider {

	void createExpressCompany(ExpressCompany expressCompany);

	void updateExpressCompany(ExpressCompany expressCompany);

	ExpressCompany findExpressCompanyById(Long id);

	List<ExpressCompany> listExpressCompany();

}