package com.everhomes.techpark.servicehotline;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.servicehotline.HotlineDTO;

public interface ServiceHotlinesProvider {

	Long createServiceHotline(ServiceHotline obj);

	void updateServiceHotline(ServiceHotline obj);

	void deleteServiceHotline(ServiceHotline obj);

	ServiceHotline getServiceHotlineById(Long id);

	List<ServiceHotline> queryServiceHotlines(Integer pageSize, ListingLocator locator,
			ListingQueryBuilderCallback queryBuilderCallback);
	
	List<ServiceHotline> queryServiceHotlines(Integer namespaceId, String ownerType, Long ownerId, Integer pageSize,
			ListingLocator locator, Long userId, String contact, Byte serviceType, Byte status);

}
