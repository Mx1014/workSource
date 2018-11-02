package com.everhomes.yellowPage;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.portal.ServiceAllianceInstanceConfig;
import com.everhomes.rest.yellowPage.GetFormListCommand;
import com.everhomes.rest.yellowPage.GetFormListResponse;
import com.everhomes.rest.yellowPage.GetWorkFlowListCommand;
import com.everhomes.rest.yellowPage.GetWorkFlowListResponse;
import com.everhomes.rest.yellowPage.standard.ConfigCommand;
import com.everhomes.yellowPage.standard.ServiceCategoryMatch;
import com.everhomes.rest.yellowPage.GetSelfDefinedStateCommand;
import com.everhomes.rest.yellowPage.GetSelfDefinedStateResponse;

public interface AllianceStandardService {

	GetFormListResponse getFormList(GetFormListCommand cmd);

	GetWorkFlowListResponse getWorkFlowList(GetWorkFlowListCommand cmd);

	void enableSelfDefinedConfig(GetSelfDefinedStateCommand cmd);
	
	void disableSelfDefinedConfig(GetSelfDefinedStateCommand cmd);
	
	GetSelfDefinedStateResponse getSelfDefinedState(GetSelfDefinedStateCommand cmd);

	List<ServiceAllianceCategories> listChildCategoriesByAdmin(CrossShardListingLocator locator, Integer pageSize,
			String ownerType, Long ownerId, Long organizationId, Long type);

	ServiceAllianceCategories queryHomePageCategoryByAdmin(String ownerType, Long ownerId, Long type);

	ServiceAllianceCategories queryHomePageCategoryByScene(Long type, Long projectId);

	ServiceAllianceCategories createHomePageCategory(String ownerType, Long ownerId, Long type);

	List<ServiceAllianceCategories> listChildCategoriesByScene(CrossShardListingLocator locator, Integer pageSize,
			String ownerType, Long ownerId, Long organizationId, Long type);

	void updateHomePageCategorysByPublish(ServiceAllianceInstanceConfig config, String name);

	ServiceCategoryMatch findServiceCategoryMatch(String ownerType, Long ownerId, Long type, Long serviceId);

	void updateMatchCategoryName(Long type, Long categoryId, String categoryName);

	boolean isDisableSelfConfig(AllianceConfigState state);

	boolean isEnableSelfConfig(AllianceConfigState state);

	Long getOrgIdByTypeAndProjectId(Long type, Long projectId);

	String transferApprovalToForm();

	String transferPadItems();

	ConfigCommand reNewConfigCommand(String ownerType, Long ownerId, Long type);

	String transferAllianceModuleUrl();

	String transferMainAllianceOwnerType();
}
