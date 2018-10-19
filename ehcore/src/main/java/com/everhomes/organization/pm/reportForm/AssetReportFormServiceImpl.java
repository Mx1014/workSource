package com.everhomes.organization.pm.reportForm;

import java.util.List;

import org.springframework.stereotype.Component;

import com.everhomes.rest.organization.pm.reportForm.BuildingReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.GetBuildingReportFormCommand;
import com.everhomes.rest.organization.pm.reportForm.GetCommunityReportFormCommand;

@Component
public class AssetReportFormServiceImpl implements AssetReportFormService{

	@Override
	public List<CommunityReportFormDTO> getCommunityReportForm(GetCommunityReportFormCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportCommunityReportForm(GetCommunityReportFormCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BuildingReportFormDTO> getBuildingReportForm(GetBuildingReportFormCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportBuildingReportForm(GetBuildingReportFormCommand cmd) {
		// TODO Auto-generated method stub
		
	}

}
