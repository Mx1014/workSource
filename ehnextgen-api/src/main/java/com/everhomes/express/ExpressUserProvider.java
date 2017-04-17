// @formatter:off
package com.everhomes.express;

import java.util.List;

import com.everhomes.rest.express.ListExpressUserCondition;

public interface ExpressUserProvider {

	void createExpressUser(ExpressUser expressUser);

	void updateExpressUser(ExpressUser expressUser);

	ExpressUser findExpressUserById(Long id);

	List<ExpressUser> listExpressUser();

	List<ExpressUser> listExpressUserByCondition(ListExpressUserCondition condition);

	ExpressUser findExpressUserByOrganizationMember(Integer namespaceId, String ownerType, Long ownerId, Long organizationId,
			Long organizationMemberId);

}