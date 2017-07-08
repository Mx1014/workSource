// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalContentScopeProvider {

	void createPortalContentScope(PortalContentScope portalContentScope);

	void updatePortalContentScope(PortalContentScope portalContentScope);

	PortalContentScope findPortalContentScopeById(Long id);

	List<PortalContentScope> listPortalContentScope(String contentType, Long contentId);

	void createPortalContentScopes(List<PortalContentScope> portalContentScopes);

	void deletePortalContentScopes(String contentType, Long contentId);

	void deletePortalContentScopeById(Long id);

	void deletePortalContentScopeByIds(List<Long> ids);
}