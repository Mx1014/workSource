package com.everhomes.energy;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.energy.EnergyStatByYearDTO;

public interface EnergyCountStatisticProvider {

	Long createEnergyCountStatistic(EnergyCountStatistic obj);

	void updateEnergyCountStatistic(EnergyCountStatistic obj);

	void deleteEnergyCountStatistic(EnergyCountStatistic obj);

	EnergyCountStatistic getEnergyCountStatisticById(Long id);

	List<EnergyCountStatistic> queryEnergyCountStatistics(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	BigDecimal getSumAmount(String statdate, Byte meterType,Byte categoryType, long categoryId );

	BigDecimal getSumCost(String statdate, Byte meterType, Byte categoryType, long categoryId);

	 
}
