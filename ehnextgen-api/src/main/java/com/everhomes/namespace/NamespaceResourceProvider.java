// @formatter:off
package com.everhomes.namespace;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.namespace.NamespaceResourceType;

/**
 * Namespace management
 *
 */
@SuppressWarnings("unchecked")
public interface NamespaceResourceProvider {
    NamespaceResource findNamespaceResourceById(Integer namespaceId);
    
    List<NamespaceResource> listResourceByNamespace(Integer namespaceId, NamespaceResourceType type);
    List<NamespaceResource> listResourceByNamespaceOrderByDefaultOrder(Integer namespaceId, NamespaceResourceType type);
    List<NamespaceResource> listResourceByNamespace(ListingLocator locator, int count, Integer namespaceId, NamespaceResourceType type);
    
    NamespaceDetail findNamespaceDetailByNamespaceId(Integer namespaceId);

	void createNamespaceResource(NamespaceResource resource);
	void deleteNamespaceResource(NamespaceResource resource);

    List<NamespaceResource> listResourceByNamespace(Integer namespaceId, NamespaceResourceType type, Long resourceId);
}
