package com.everhomes.energy;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface EnergyDateStatisticProvider {

	Long createEnergyDateStatistic(EnergyDateStatistic obj);

	void updateEnergyDateStatistic(EnergyDateStatistic obj);

	void deleteEnergyDateStatistic(EnergyDateStatistic obj);

	EnergyDateStatistic getEnergyDateStatisticById(Long id);

	List<EnergyDateStatistic> queryEnergyDateStatistics(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

}
