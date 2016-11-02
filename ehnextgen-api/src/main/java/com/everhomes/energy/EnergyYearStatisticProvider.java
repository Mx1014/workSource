package com.everhomes.energy;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface EnergyYearStatisticProvider {

	Long createEnergyYearStatistic(EnergyYearStatistic obj);

	void updateEnergyYearStatistic(EnergyYearStatistic obj);

	void deleteEnergyYearStatistic(EnergyYearStatistic obj);

	EnergyYearStatistic getEnergyYearStatisticById(Long id);

	List<EnergyYearStatistic> queryEnergyYearStatistics(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

}
