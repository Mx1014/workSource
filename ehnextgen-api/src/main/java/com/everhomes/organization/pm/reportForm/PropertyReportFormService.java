package com.everhomes.organization.pm.reportForm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.everhomes.rest.organization.pm.reportForm.BuildingBriefStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.BuildingReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.BuildingTotalStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityBriefStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityTotalStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.GetBuildingReportFormCommand;
import com.everhomes.rest.organization.pm.reportForm.GetCommunityReportFormCommand;
import com.everhomes.rest.organization.pm.reportForm.GetTotalBuildingStaticsCommand;
import com.everhomes.rest.organization.pm.reportForm.GetTotalCommunityStaticsCommand;
import com.everhomes.rest.organization.pm.reportForm.ListBuildingReportFormResponse;
import com.everhomes.rest.organization.pm.reportForm.ListCommunityReportFormResponse;
import com.everhomes.rest.organization.pm.reportForm.TotalBuildingStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.TotalCommunityStaticsDTO;

public interface PropertyReportFormService {

	ListCommunityReportFormResponse getCommunityReportForm(GetCommunityReportFormCommand cmd);

	void exportCommunityReportForm(GetTotalCommunityStaticsCommand cmd);

	ListBuildingReportFormResponse getBuildingReportForm(GetBuildingReportFormCommand cmd);

	void exportBuildingReportForm(GetTotalBuildingStaticsCommand cmd);
	
	Map<Long, CommunityBriefStaticsDTO> listCommunityBriefStaticsForBill(Integer namespaceId, List<Long> communityIdList,
			String dateStr);

	CommunityTotalStaticsDTO getTotalCommunityBriefStaticsForBill(Integer namespaceId, List<Long> communityIdList,
			String dateStr);

	Map<String,BuildingBriefStaticsDTO> listBuildingBriefStaticsForBill(Integer namespaceId, Long communityId,
			List<String> buildingNameList, String dateStr);

	BuildingTotalStaticsDTO getTotalBuildingBriefStaticsForBill(Integer namespaceId, Long communityId,
			List<String> buildingNameList, String dateStr);

	TotalCommunityStaticsDTO getTotalCommunityStatics(GetTotalCommunityStaticsCommand cmd);

	TotalBuildingStaticsDTO getTotalBuildingStatics(GetTotalBuildingStaticsCommand cmd);

	Map<String, BigDecimal> getChargeAreaByContractIdAndAddress(List<Long> contractIds, List<String> buildindNames,
			List<String> apartmentNames);

	BigDecimal getTotalChargeArea(List<Long> contractIds, List<String> buildindNames, List<String> apartmentNames);
}
