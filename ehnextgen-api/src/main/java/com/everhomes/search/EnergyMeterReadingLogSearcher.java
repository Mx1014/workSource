package com.everhomes.search;

import com.everhomes.energy.EnergyMeterReadingLog;
import com.everhomes.rest.energy.SearchEnergyMeterReadingLogsCommand;
import com.everhomes.rest.energy.SearchEnergyMeterReadingLogsResponse;

import java.util.List;

public interface EnergyMeterReadingLogSearcher {

	void deleteById(Long id);

    void bulkUpdate(List<EnergyMeterReadingLog> meters);

    void feedDoc(EnergyMeterReadingLog meter);

    void syncFromDb();

    SearchEnergyMeterReadingLogsResponse queryMeterReadingLogs(SearchEnergyMeterReadingLogsCommand cmd);
}
