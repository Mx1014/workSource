package com.everhomes.energy;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public interface EnergyDateStatisticProvider {

	Long createEnergyDateStatistic(EnergyDateStatistic obj);

	void updateEnergyDateStatistic(EnergyDateStatistic obj);

	void deleteEnergyDateStatistic(EnergyDateStatistic obj);

	EnergyDateStatistic getEnergyDateStatisticById(Long id);

	List<EnergyDateStatistic> queryEnergyDateStatistics(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	void deleteEnergyDateStatisticByDate(Long meterId, Date date);
 

	BigDecimal getSumAmountBetweenDate(Long meterId, Date begin, Date end);

	BigDecimal getSumCostBetweenDate(Long meterId ,Date begin, Date end);
 

	List<EnergyDateStatistic> listEnergyDateStatistics(Byte meterType, Long communityId, List<Long> billCategoryIds,
			List<Long> serviceCategoryIds, Date startDate, Date endDate);

    EnergyDateStatistic findByMeterAndDate(Integer namespaceId, Long meterId, Date date);

	EnergyDateStatistic getEnergyDateStatisticByStatDate(Long meterId, Date statDate);
}
