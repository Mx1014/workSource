// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalContentScopeProvider {

	void createPortalContentScope(PortalContentScope portalContentScope);

	void updatePortalContentScope(PortalContentScope portalContentScope);

	PortalContentScope findPortalContentScopeById(Long id);

	List<PortalContentScope> listPortalContentScope();

	void createPortalContentScopes(List<PortalContentScope> portalContentScopes);
}