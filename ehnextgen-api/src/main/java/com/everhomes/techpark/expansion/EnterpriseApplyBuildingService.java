package com.everhomes.techpark.expansion;

import com.everhomes.rest.techpark.expansion.*;

import java.util.List;

/**
 * @author sw on 2017/8/3.
 */
public interface EnterpriseApplyBuildingService {

    ListLeaseBuildingsResponse listLeaseBuildings(ListLeaseBuildingsCommand cmd);

    LeaseBuildingDTO createLeaseBuilding(CreateLeaseBuildingCommand cmd);

    LeaseBuildingDTO updateLeaseBuilding(UpdateLeaseBuildingCommand cmd);

    LeaseBuildingDTO getLeaseBuildingById(GetLeaseBuildingByIdCommand cmd);

    void deleteLeaseBuilding(DeleteLeaseBuildingCommand cmd);

    void updateLeaseBuildingOrder(UpdateLeaseBuildingOrderCommand cmd);

    void syncLeaseBuildings(ListLeaseBuildingsCommand cmd);

    List<BriefLeaseProjectDTO> listAllLeaseProjects(ListAllLeaseProjectsCommand cmd);

    listLeaseProjectsResponse listLeaseProjects(ListLeaseProjectsCommand cmd);

    LeaseProjectDTO updateLeaseProject(UpdateLeaseProjectCommand cmd);

    LeaseProjectDTO getLeaseProjectById(GetLeaseProjectByIdCommand cmd);
}
