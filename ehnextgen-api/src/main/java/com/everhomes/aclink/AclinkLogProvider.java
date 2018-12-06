package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.*;

public interface AclinkLogProvider {

    Long createAclinkLog(AclinkLog obj);

    void updateAclinkLog(AclinkLog obj);

    void deleteAclinkLog(AclinkLog obj);

    AclinkLog getAclinkLogById(Long id);

    List<AclinkLog> queryAclinkLogs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

	List<AclinkLog> queryAclinkLogsByTime(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);
	//join eh_door_auth
    List<AclinkLogDTO> queryAclinkLogDTOsByTime(ListingLocator locator, int count,
                                             ListingQueryBuilderCallback queryBuilderCallback);
    //add by liqingyan
    DoorStatisticDTO queryDoorStatistic(DoorStatisticCommand cmd);
    //add by liqingyan
    List<DoorStatisticByTimeDTO> queryDoorStatisticByTime(DoorStatisticByTimeCommand cmd);

    List<TempStatisticByTimeDTO> queryTempStatisticByTime(TempStatisticByTimeCommand cmd);

	void createAclinkLogBatch(List<AclinkLog> logs);

}
