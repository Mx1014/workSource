package com.everhomes.energy;
 
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.List;

public interface EnergyMeterReadingLogProvider {

	Long createEnergyMeterReadingLog(EnergyMeterReadingLog obj);

	// void updateEnergyMeterReadingLog(EnergyMeterReadingLog obj);

	void deleteEnergyMeterReadingLog(EnergyMeterReadingLog obj);

	EnergyMeterReadingLog getEnergyMeterReadingLogById(Long id);

	List<EnergyMeterReadingLog> queryEnergyMeterReadingLogs(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	List<EnergyMeterReadingLog> listMeterReadingLogByDate(Long id, Timestamp startBegin, Timestamp endBegin);

	EnergyMeterReadingLog getLastMeterReadingLogByDate(Long id, Timestamp startBegin, Timestamp endBegin);

    List<EnergyMeterReadingLog> listMeterReadingLogs(long pageAnchor, int pageSize);

    EnergyMeterReadingLog findLastReadingLogByMeterId(Integer namespaceId, Long meterId);

    /**
     * 获取表记的所有读表记录
     */
    List<EnergyMeterReadingLog> listMeterReadingLogsByMeterId(Integer namespaceId, Long meterId);

    void deleteMeterReadingLogsByMeterId(Integer namespaceId, Long meterId);

    // List<EnergyMeterReadingLog> listMeterReadingLogs(Integer namespaceId, Long meterId, Long pageAnchor, int pageSize);
}
