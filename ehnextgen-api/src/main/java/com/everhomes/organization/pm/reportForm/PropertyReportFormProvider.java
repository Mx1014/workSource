package com.everhomes.organization.pm.reportForm;

import java.util.List;
import java.util.Map;

import com.everhomes.rest.organization.pm.reportForm.BuildingBriefStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.BuildingReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.BuildingTotalStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityBriefStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityTotalStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.TotalBuildingStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.TotalCommunityStaticsDTO;

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

	List<CommunityReportFormDTO> listCommunityReportForm(Integer namespaceId, List<Long> communityIds, String dateStr,
			Integer pageOffSet, Integer pageSize);

	TotalCommunityStaticsDTO getTotalCommunityStatics(Integer namespaceId, List<Long> communityIds,
			String formatDateStr);

	List<BuildingReportFormDTO> listBuildingReportForm(Integer namespaceId, Long communityId, List<Long> buildingIds,
			String formatDateStr, Integer pageOffSet, Integer pageSize);

	TotalBuildingStaticsDTO getTotalBuildingStatics(Integer namespaceId, Long communityId, List<Long> buildingIds,
			String formatDateStr);

	void deleteBuildingDataByDateStr(String dateStr);

	void deleteCommunityDataByDateStr(String dateStr);

}
