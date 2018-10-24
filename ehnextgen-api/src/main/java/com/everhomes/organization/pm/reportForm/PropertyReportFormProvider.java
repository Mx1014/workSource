package com.everhomes.organization.pm.reportForm;

import java.util.List;
import java.util.Map;

import com.everhomes.rest.organization.pm.reportForm.BuildingBriefStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.BuildingTotalStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityBriefStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityTotalStaticsDTO;

public interface PropertyReportFormProvider {

	void createBuildingStatistics(BuildingStatistics buildingStatistics);

	void createCommunityStatics(CommunityStatistics communityStatistics);

	Map<Long, CommunityBriefStaticsDTO> listCommunityBriefStaticsForBill(Integer namespaceId,
			List<Long> communityIdList, String dateStr);

	CommunityTotalStaticsDTO getTotalCommunityBriefStaticsForBill(Integer namespaceId, List<Long> communityIdList,
			String dateStr);

	Map<String, BuildingBriefStaticsDTO> listBuildingBriefStaticsForBill(Integer namespaceId, Long communityId,
			List<String> buildingNameList, String dateStr);

	BuildingTotalStaticsDTO getTotalBuildingBriefStaticsForBill(Integer namespaceId, Long communityId,
			List<String> buildingNameList, String dateStr);

}
