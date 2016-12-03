// @formatter:off
package com.everhomes.openapi;

import java.util.List;

public interface AppNamespaceMappingProvider {

	void createAppNamespaceMapping(AppNamespaceMapping appNamespaceMapping);

	void updateAppNamespaceMapping(AppNamespaceMapping appNamespaceMapping);

	AppNamespaceMapping findAppNamespaceMappingById(Long id);

	List<AppNamespaceMapping> listAppNamespaceMapping();

	AppNamespaceMapping findAppNamespaceMappingByAppKey(String appKey);

}