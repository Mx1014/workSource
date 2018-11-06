// @formatter:off
package com.everhomes.openapi;

import com.everhomes.app.App;
import com.everhomes.listing.ListingLocator;

import java.util.List;

public interface AppNamespaceMappingProvider {

	void createAppNamespaceMapping(AppNamespaceMapping appNamespaceMapping);

	void updateAppNamespaceMapping(AppNamespaceMapping appNamespaceMapping);

	AppNamespaceMapping findAppNamespaceMappingById(Long id);

	List<AppNamespaceMapping> listAppNamespaceMapping(Integer namespaceId, int pageSize, ListingLocator locator);

	AppNamespaceMapping findAppNamespaceMappingByAppKey(String appKey);

    List<App> listAppsByAppKey(List<String> appKeys);

	List<App> listAppsByExcludeAppKey(List<String> appKeys);

    void deleteNamespaceMapping(AppNamespaceMapping mapping);

	AppNamespaceMapping findAppNamespaceMappingByNamespaceId(Integer namespaceId);
}