package com.everhomes.yellowPage;

import com.everhomes.rest.yellowPage.GetFormListCommand;
import com.everhomes.rest.yellowPage.GetFormListResponse;
import com.everhomes.rest.yellowPage.GetWorkFlowListCommand;
import com.everhomes.rest.yellowPage.GetWorkFlowListResponse;

public interface AllianceStandardService {

	GetFormListResponse getFormList(GetFormListCommand cmd);

	GetWorkFlowListResponse getWorkFlowList(GetWorkFlowListCommand cmd);

}
