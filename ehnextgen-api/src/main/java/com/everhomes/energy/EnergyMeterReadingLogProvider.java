package com.everhomes.energy;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface EnergyMeterReadingLogProvider {

	Long createEnergyMeterReadingLog(EnergyMeterReadingLog obj);

	void updateEnergyMeterReadingLog(EnergyMeterReadingLog obj);

	void deleteEnergyMeterReadingLog(EnergyMeterReadingLog obj);

	EnergyMeterReadingLog getEnergyMeterReadingLogById(Long id);

	List<EnergyMeterReadingLog> queryEnergyMeterReadingLogs(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	List<EnergyMeterReadingLog> listMeterReadingLogByDate(Long id, Timestamp startBegin, Timestamp endBegin);

	EnergyMeterReadingLog getLastMeterReadingLogByDate(Long id, Timestamp startBegin, Timestamp endBegin);

}
