package com.everhomes.techpark.punch;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface PunchSchedulingProvider {

	Long createPunchScheduling(PunchScheduling obj);

	void updatePunchScheduling(PunchScheduling obj);

	void deletePunchScheduling(PunchScheduling obj);

	PunchScheduling getPunchSchedulingById(Long id);

	List<PunchScheduling> queryPunchSchedulings(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

}
