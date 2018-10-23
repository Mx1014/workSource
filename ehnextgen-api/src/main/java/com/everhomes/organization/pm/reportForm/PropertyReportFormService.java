package com.everhomes.organization.pm.reportForm;

import java.util.List;

import com.everhomes.rest.organization.pm.reportForm.BuildingBriefStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.BuildingReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.BuildingTotalStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityBriefStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityTotalStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.GetBuildingReportFormCommand;
import com.everhomes.rest.organization.pm.reportForm.GetCommunityReportFormCommand;

public interface PropertyReportFormService {

	List<CommunityReportFormDTO> getCommunityReportForm(GetCommunityReportFormCommand cmd);

	void exportCommunityReportForm(GetCommunityReportFormCommand cmd);

	List<BuildingReportFormDTO> getBuildingReportForm(GetBuildingReportFormCommand cmd);

	void exportBuildingReportForm(GetBuildingReportFormCommand cmd);
	
	List<CommunityBriefStaticsDTO> listCommunityBriefStaticsForBill(Integer namespaceId, List<Long> communityIdList,
			String dateStr);

	CommunityTotalStaticsDTO getTotalCommunityBriefStaticsForBill(Integer namespaceId, List<Long> communityIdList,
			String dateStr);

	List<BuildingBriefStaticsDTO> listBuildingBriefStaticsForBill(Integer namespaceId, Long communityId,
			List<String> buildingNameList, String dateStr);

	BuildingTotalStaticsDTO getTotalBuildingBriefStaticsForBill(Integer namespaceId, Long communityId,
			List<String> buildingNameList, String dateStr);
}
