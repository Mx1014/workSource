package com.everhomes.energy;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface EnergyCountStatisticProvider {

	Long createEnergyCountStatistic(EnergyCountStatistic obj);

	void updateEnergyCountStatistic(EnergyCountStatistic obj);

	void deleteEnergyCountStatistic(EnergyCountStatistic obj);

	EnergyCountStatistic getEnergyCountStatisticById(Long id);

	List<EnergyCountStatistic> queryEnergyCountStatistics(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

}
