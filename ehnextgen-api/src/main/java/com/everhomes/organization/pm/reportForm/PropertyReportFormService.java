package com.everhomes.organization.pm.reportForm;

import java.util.List;

import com.everhomes.rest.organization.pm.reportForm.BuildingReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.GetBuildingReportFormCommand;
import com.everhomes.rest.organization.pm.reportForm.GetCommunityReportFormCommand;

public interface PropertyReportFormService {

	List<CommunityReportFormDTO> getCommunityReportForm(GetCommunityReportFormCommand cmd);

	void exportCommunityReportForm(GetCommunityReportFormCommand cmd);

	List<BuildingReportFormDTO> getBuildingReportForm(GetBuildingReportFormCommand cmd);

	void exportBuildingReportForm(GetBuildingReportFormCommand cmd);

}
