package com.everhomes.organization.pm.reportForm;

import java.util.Collection;

public interface PropertyReportFormProvider {

	void createBuildingStatistics(BuildingStatistics buildingStatistics);

	void createCommunityStatics(CommunityStatistics communityStatistics);

	void batchAddBuildingStatistics(Collection<BuildingStatistics> values);

	void batchAddCommunityStatics(Collection<CommunityStatistics> values);

}
