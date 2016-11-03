package com.everhomes.energy;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface EnergyMonthStatisticProvider {

	Long createEnergyMonthStatistic(EnergyMonthStatistic obj);

	void updateEnergyMonthStatistic(EnergyMonthStatistic obj);

	void deleteEnergyMonthStatistic(EnergyMonthStatistic obj);

	EnergyMonthStatistic getEnergyMonthStatisticById(Long id);

	List<EnergyMonthStatistic> queryEnergyMonthStatistics(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	void deleteEnergyMonthStatisticByDate(Long meterId, String monthStr);

	List<EnergyCountStatistic> listEnergyCountStatistic(String monthStr);

}
