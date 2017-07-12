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

	/**
	 * 固定收费部分直接把每天的相加
	 * @param meterId
	 * @param begin
	 * @param end
     * @return
     */
	BigDecimal getSumCostBetweenDate(Long meterId ,Date begin, Date end);
 

	List<EnergyDateStatistic> listEnergyDateStatistics(Byte meterType, Long communityId, List<Long> billCategoryIds,
			List<Long> serviceCategoryIds, Date startDate, Date endDate);

    EnergyDateStatistic findByMeterAndDate(Integer namespaceId, Long meterId, Date date);

	EnergyDateStatistic getEnergyDateStatisticByStatDate(Long meterId, Date statDate);

	/**
	 * 获取分段部分的消耗量
	 * @param meterId
	 * @param begin
	 * @param end
     * @return
     */
	List<BigDecimal> getBlockSumAmountBetweenDate(Long meterId, Date begin, Date end);


}
