package com.everhomes.techpark.servicehotline;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface ServiceHotlinesProvider {

	Long createServiceHotline(ServiceHotline obj);

	void updateServiceHotline(ServiceHotline obj);

	void deleteServiceHotline(ServiceHotline obj);

	ServiceHotline getServiceHotlineById(Long id);

	List<ServiceHotline> queryServiceHotlines(ListingLocator locator,
			int count, ListingQueryBuilderCallback queryBuilderCallback);

}
