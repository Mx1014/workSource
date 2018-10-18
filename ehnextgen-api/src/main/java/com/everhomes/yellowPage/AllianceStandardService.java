package com.everhomes.yellowPage;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.yellowPage.GetFormListCommand;
import com.everhomes.rest.yellowPage.GetFormListResponse;
import com.everhomes.rest.yellowPage.GetWorkFlowListCommand;
import com.everhomes.rest.yellowPage.GetWorkFlowListResponse;
import com.everhomes.rest.yellowPage.GetSelfDefinedStateCommand;
import com.everhomes.rest.yellowPage.GetSelfDefinedStateResponse;

public interface AllianceStandardService {

	GetFormListResponse getFormList(GetFormListCommand cmd);

	GetWorkFlowListResponse getWorkFlowList(GetWorkFlowListCommand cmd);

	void enableSelfDefinedConfig(GetSelfDefinedStateCommand cmd);
	
	void disableSelfDefinedConfig(GetSelfDefinedStateCommand cmd);
	
	GetSelfDefinedStateResponse getSelfDefinedState(GetSelfDefinedStateCommand cmd);

	ServiceAlliances queryServiceAllianceTopic(String ownerType, Long ownerId, Long type);

	List<ServiceAllianceCategories> listChildCategories(CrossShardListingLocator locator, Integer pageSize,
			String ownerType, Long ownerId, Long organizationId, Long type);

	ServiceAllianceCategories queryServiceAllianceCategoryTopic(String ownerType, Long ownerId, Long type);
}
