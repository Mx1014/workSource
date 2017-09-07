package com.everhomes.techpark.servicehotline;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface ServiceConfigurationsProvider {

	Long createServiceConfiguration(ServiceConfiguration obj);

	void updateServiceConfiguration(ServiceConfiguration obj);

	void deleteServiceConfiguration(ServiceConfiguration obj);

	ServiceConfiguration getServiceConfigurationById(Long id);

	List<ServiceConfiguration> queryServiceConfigurations(
			ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	void deleteServiceConfiguration(Long id);

    ServiceConfiguration getServiceConfiguration(Integer namespaceId, String ownerType, Long ownerId, String confName);
}
