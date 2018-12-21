// @formatter:off
package com.everhomes.enterprisemoment;

import java.util.List;

public interface EnterpriseMomentScopeProvider {

	void createEnterpriseMomentScope(EnterpriseMomentScope enterpriseMomentScope);

	void updateEnterpriseMomentScope(EnterpriseMomentScope enterpriseMomentScope);

	EnterpriseMomentScope findEnterpriseMomentScopeById(Long id);

	List<EnterpriseMomentScope> listEnterpriseMomentScope();

	List<EnterpriseMomentScope> listEnterpriseMomentScopeByMomentId(Integer namespaceId, Long organizationId, Long momentId);
}