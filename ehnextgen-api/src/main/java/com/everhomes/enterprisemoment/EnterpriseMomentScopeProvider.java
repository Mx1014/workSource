// @formatter:off
package com.everhomes.enterprisemoment;

import java.util.List;

public interface EnterpriseMomentScopeProvider {

	void createEnterpriseMomentScope(EnterpriseMomentScope enterpriseMomentScope);

	List<EnterpriseMomentScope> listEnterpriseMomentScopeByMomentId(Integer namespaceId, Long organizationId, Long momentId);
}