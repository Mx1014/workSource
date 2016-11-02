package com.everhomes.energy;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

/**
 * Created by xq.tian on 2016/10/25.
 */
public interface EnergyMeterChangeLogProvider {

	Long createEnergyMeterChangeLog(EnergyMeterChangeLog obj);

	void updateEnergyMeterChangeLog(EnergyMeterChangeLog obj);

	void deleteEnergyMeterChangeLog(EnergyMeterChangeLog obj);

	EnergyMeterChangeLog getEnergyMeterChangeLogById(Long id);

	List<EnergyMeterChangeLog> queryEnergyMeterChangeLogs(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	EnergyMeterChangeLog getEnergyMeterChangeLogByLogId(Long id);

}
