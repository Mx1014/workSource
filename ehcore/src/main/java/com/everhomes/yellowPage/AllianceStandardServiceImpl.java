package com.everhomes.yellowPage;

import org.springframework.stereotype.Component;

import com.everhomes.rest.yellowPage.GetFormListCommand;
import com.everhomes.rest.yellowPage.GetFormListResponse;
import com.everhomes.rest.yellowPage.GetWorkFlowListCommand;
import com.everhomes.rest.yellowPage.GetWorkFlowListResponse;
import com.everhomes.rest.yellowPage.UpdateProjectConfigFlagCommand;

@Component
public class AllianceStandardServiceImpl implements AllianceStandardService{

	@Override
	public GetFormListResponse getFormList(GetFormListCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetWorkFlowListResponse getWorkFlowList(GetWorkFlowListCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateServiceAllianceConfigFlag(UpdateProjectConfigFlagCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCategoryConfigFlag(UpdateProjectConfigFlagCommand cmd) {
		// TODO Auto-generated method stub
		
	}

}
