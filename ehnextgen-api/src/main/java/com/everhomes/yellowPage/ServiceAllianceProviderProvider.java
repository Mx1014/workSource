// @formatter:off
package com.everhomes.yellowPage;

import java.util.List;
import java.util.Map;

import com.everhomes.flow.FlowEvaluate;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface ServiceAllianceProviderProvider {

	void createServiceAllianceProvid(ServiceAllianceProvid serviceAllianceProvider);

	void updateServiceAllianceProvid(ServiceAllianceProvid serviceAllianceProvider);

	void deleteServiceAllianceProvid(ServiceAllianceProvid serviceAllianceProvider);

	ServiceAllianceProvid findServiceAllianceProvidById(Long id);

	List<ServiceAllianceProvid> listServiceAllianceProviders(ListingLocator locator, Integer pageSize,
			Integer namespaceId, String ownerType, Long ownerId, Long type, Long categoryId, String keyword);

	List<ServiceAllianceProvid> listServiceAllianceProviders(ListingLocator locator, Integer pageSize,
			ListingQueryBuilderCallback callback);
	
	void updateScoreByEvaluation(Long flowCaseId, FlowEvaluate evaluate);
}
	
