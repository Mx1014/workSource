package com.everhomes.energy;

import java.math.BigDecimal;
import java.sql.Date;
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

	void deleteEnergyDateStatisticByDate(Long meterId, Date date);

	EnergyDateStatistic getEnergyDateStatisticByStatDate(Date statDate);

	BigDecimal getSumAmountBetweenDate(Date begin, Date end);

	BigDecimal getSumCostBetweenDate(Date begin, Date end);

}
