// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalLayoutTemplateProvider {

	void createPortalLayoutTemplate(PortalLayoutTemplate portalLayoutTemplate);

	void updatePortalLayoutTemplate(PortalLayoutTemplate portalLayoutTemplate);

	PortalLayoutTemplate findPortalLayoutTemplateById(Long id);

	List<PortalLayoutTemplate> listPortalLayoutTemplate();

}