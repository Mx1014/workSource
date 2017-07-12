package com.everhomes.techpark.expansion;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface EnterpriseOpRequestBuildingProvider {

	Long createEnterpriseOpRequestBuilding(EnterpriseOpRequestBuilding obj);

	void updateEnterpriseOpRequestBuilding(EnterpriseOpRequestBuilding obj);

	void deleteEnterpriseOpRequestBuilding(EnterpriseOpRequestBuilding obj);

	EnterpriseOpRequestBuilding getEnterpriseOpRequestBuildingById(Long id);

	List<EnterpriseOpRequestBuilding> queryEnterpriseOpRequestBuildings(ListingLocator locator,
			int count, ListingQueryBuilderCallback queryBuilderCallback);

}
