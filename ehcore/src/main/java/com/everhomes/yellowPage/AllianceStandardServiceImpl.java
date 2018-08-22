package com.everhomes.yellowPage;

import org.springframework.stereotype.Component;

import com.everhomes.rest.yellowPage.GetFormListCommand;
import com.everhomes.rest.yellowPage.GetFormListResponse;
import com.everhomes.rest.yellowPage.GetWorkFlowListCommand;
import com.everhomes.rest.yellowPage.GetWorkFlowListResponse;
import com.everhomes.rest.yellowPage.GetSelfDefinedStateCommand;
import com.everhomes.rest.yellowPage.GetSelfDefinedStateResponse;

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
	public void enableSelfDefinedConfig(GetSelfDefinedStateCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableSelfDefinedConfig(GetSelfDefinedStateCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GetSelfDefinedStateResponse getSelfDefinedState(GetSelfDefinedStateCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}



}
