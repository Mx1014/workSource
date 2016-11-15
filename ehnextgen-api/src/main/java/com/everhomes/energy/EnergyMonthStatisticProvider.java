package com.everhomes.energy;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface EnergyMonthStatisticProvider {

	Long createEnergyMonthStatistic(EnergyMonthStatistic obj);

	void updateEnergyMonthStatistic(EnergyMonthStatistic obj);

	void deleteEnergyMonthStatistic(EnergyMonthStatistic obj);

	EnergyMonthStatistic getEnergyMonthStatisticById(Long id);

	List<EnergyMonthStatistic> queryEnergyMonthStatistics(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	void deleteEnergyMonthStatisticByDate(Long meterId, String monthStr);

	List<EnergyCountStatistic> listEnergyCountStatistic(String monthStr);

	List<EnergyMonthStatistic> listEnergyMonthStatistics(Byte meterType, Long communityId, List<Long> billCategoryIds,
			List<Long> serviceCategoryIds, String yearStr);

    EnergyMonthStatistic findByMeterAndDate(Integer namespaceId, Long meterId, String dateStr);
}
