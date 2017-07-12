package com.everhomes.energy;

import java.sql.Date;
import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface EnergyYoyStatisticProvider {

	Long createEnergyYoyStatistic(EnergyYoyStatistic obj);

	void updateEnergyYoyStatistic(EnergyYoyStatistic obj);

	void deleteEnergyYoyStatistic(EnergyYoyStatistic obj);

	EnergyYoyStatistic getEnergyYoyStatisticById(Long id);

	List<EnergyYoyStatistic> queryEnergyYoyStatistics(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	List<EnergyYoyStatistic>  listenergyYoyStatistics(Integer currentNamespaceId, String dateStr);
 
	EnergyYoyStatistic getEnergyYoyStatisticByCommuniytyAndDate(Long communityId, String date);

	void deleteEnergyYoyStatistic(Long communityId, String dateStr);

}
