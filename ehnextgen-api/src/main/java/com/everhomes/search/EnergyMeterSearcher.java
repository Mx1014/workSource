package com.everhomes.search;

import com.everhomes.energy.EnergyMeter;
import com.everhomes.rest.energy.SearchEnergyMeterCommand;
import com.everhomes.rest.energy.SearchEnergyMeterResponse;

import java.util.List;

public interface EnergyMeterSearcher {

	void deleteById(Long id);
    void bulkUpdate(List<EnergyMeter> meters);
    void feedDoc(EnergyMeter meter);
    void syncFromDb();
    SearchEnergyMeterResponse queryMeters(SearchEnergyMeterCommand cmd);
    List<Long> getMeterIds(SearchEnergyMeterCommand cmd);
}
