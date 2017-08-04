package com.everhomes.techpark.expansion;

import com.everhomes.rest.techpark.expansion.*;

/**
 * @author sw on 2017/8/3.
 */
public interface EnterpriseApplyBuildingService {

    ListLeaseBuildingsResponse listLeaseBuildings(ListLeaseBuildingsCommand cmd);

    LeaseBuildingDTO createLeaseBuilding(CreateLeaseBuildingCommand cmd);

    LeaseBuildingDTO updateLeaseBuilding(UpdateLeaseBuildingCommand cmd);

    LeaseBuildingDTO getLeaseBuildingById(GetLeaseBuildingByIdCommand cmd);

    void deleteLeaseBuilding(DeleteLeaseBuildingCommand cmd);
}
