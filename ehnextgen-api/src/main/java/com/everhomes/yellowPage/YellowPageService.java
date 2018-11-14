package com.everhomes.yellowPage;


import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.common.PrivilegeType;
import com.everhomes.rest.portal.ServiceAllianceInstanceConfig;
import com.everhomes.rest.yellowPage.*;
import com.everhomes.rest.yellowPage.stat.ClickStatDTO;
import com.everhomes.rest.yellowPage.stat.ClickTypeDTO;
import com.everhomes.rest.yellowPage.stat.InterestStatDTO;
import com.everhomes.rest.yellowPage.stat.ListClickStatCommand;
import com.everhomes.rest.yellowPage.stat.ListClickStatDetailCommand;
import com.everhomes.rest.yellowPage.stat.ListClickStatDetailResponse;
import com.everhomes.rest.yellowPage.stat.ListClickStatResponse;
import com.everhomes.rest.yellowPage.stat.ListInterestStatResponse;
import com.everhomes.rest.yellowPage.stat.ListServiceTypeNamesCommand;
import com.everhomes.rest.yellowPage.stat.ListStatCommonCommand;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceCategories;
import com.everhomes.server.schema.tables.pojos.EhServiceAlliances;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

public interface YellowPageService {

	public static final String SERVICE_ALLIANCE_HANDLER_NAME = "service_alliance";
	
	public final String SERVICE_ATTACH_OWNER_TYPE = EhServiceAlliances.class.getSimpleName();
	public final String HOME_PAGE_ATTACH_OWNER_TYPE = EhServiceAllianceCategories.class.getSimpleName();

	public final long SERVICE_ALLIANCE_MODULE_ID = 40500L;

	YellowPageDTO getYellowPageDetail(GetYellowPageDetailCommand cmd);

	YellowPageListResponse getYellowPageList(GetYellowPageListCommand cmd);
 

	void addYellowPage(AddYellowPageCommand cmd);

	void deleteYellowPage(DeleteYellowPageCommand cmd);

	void updateYellowPage(UpdateYellowPageCommand cmd);

	YellowPageDTO getYellowPageTopic(GetYellowPageTopicCommand cmd);
	
	void updateServiceAllianceCategory(UpdateServiceAllianceCategoryCommand cmd);
	void deleteServiceAllianceCategory(DeleteServiceAllianceCategoryCommand cmd);

	ServiceAllianceDTO getServiceAllianceEnterpriseDetail(GetServiceAllianceEnterpriseDetailCommand cmd);
	ServiceAllianceListResponse getServiceAllianceEnterpriseList(GetServiceAllianceEnterpriseListCommand cmd);
	void updateServiceAlliance(UpdateServiceAllianceCommand cmd);
	void deleteServiceAllianceEnterprise(DeleteServiceAllianceEnterpriseCommand cmd);
	void updateServiceAllianceEnterprise(UpdateServiceAllianceEnterpriseCommand cmd);
	
	void addTarget(AddNotifyTargetCommand cmd);
	void deleteNotifyTarget(DeleteNotifyTargetCommand cmd);
	void setNotifyTargetStatus(SetNotifyTargetStatusCommand cmd);
	ListNotifyTargetsResponse listNotifyTargets(ListNotifyTargetsCommand cmd);
	void verifyNotifyTarget(VerifyNotifyTargetCommand cmd);

    /**
     * 根据parentId查询服务联盟类型
     * @param cmd
     * @return
     */
	List<ServiceAllianceCategoryDTO> listServiceAllianceCategories(ListServiceAllianceCategoriesCommand cmd);


    /**
     * 获取服务联盟机构的展示类型
     * (用于客户端服务联盟类型displayType配置为list时获取displayMode使用)
     */
    ServiceAllianceDisplayModeDTO getServiceAllianceDisplayMode(GetServiceAllianceDisplayModeCommand cmd);


	List<JumpModuleDTO> listJumpModules(ListJumpModulesCommand cmd);
	
	ListAttachmentsResponse listAttachments(ListAttachmentsCommand cmd);
	
	/**
	 * 设置显示或者隐藏服务联盟企业
	 */
	void updateServiceAllianceEnterpriseDisplayFlag(UpdateServiceAllianceEnterpriseDisplayFlagCommand cmd);

	/**
	 * 交换cmd中两个服务联盟的sortOrder
	 */
	ServiceAllianceListResponse updateServiceAllianceEnterpriseDefaultOrder(UpdateServiceAllianceEnterpriseDefaultOrderCommand cmd);

	GetCategoryIdByEntryIdResponse getCategoryIdByEntryId(GetCategoryIdByEntryIdCommand cmd);

	void syncOldForm();

	void syncServiceAllianceApplicationRecords();
	
	SearchRequestInfoResponse listSeviceAllianceAppRecordsByEnterpriseId(Long enterpriseId, Long pageAnchor,
			Integer pageSize);
	
	ListServiceAllianceProvidersResponse listServiceAllianceProviders(ListServiceAllianceProvidersCommand cmd);
	void addServiceAllianceProvider(AddServiceAllianceProviderCommand cmd);
	void deleteServiceAllianceProvider(DeleteServiceAllianceProviderCommand cmd);
	void updateServiceAllianceProvider(UpdateServiceAllianceProviderCommand cmd);
	
	void applyExtraAllianceEvent(ApplyExtraAllianceEventCommand cmd);
	GetExtraAllianceEventResponse getExtraAllianceEvent(GetExtraAllianceEventCommand cmd);
	
	void exportServiceAllianceProviders(ListServiceAllianceProvidersCommand cmd,  HttpServletResponse httpResp);
	
	String transferPosterUriToAttachment();


	ListOnlineServicesResponse listOnlineServices(ListOnlineServicesCommand cmd);


	void checkPrivilege(PrivilegeType privilegeType, Long currentPMId, Long appId, Long checkCommunityId);

	void updateAllianceTag(UpdateAllianceTagCommand cmd);

	GetAllianceTagResponse getAllianceTagList(GetAllianceTagCommand cmd);

	String transferLaunchPadItems();

	String transferTime(Long parentId);

	ListServiceAllianceCategoriesAdminResponse listServiceAllianceCategoriesByAdmin(ListServiceAllianceCategoriesCommand cmd);

	void updateServiceTypeOrders(UpdateServiceTypeOrdersCommand cmd);

	void updateAllianceTag(Integer namespaceId, String ownerType, Long ownerId, Long type, AllianceTagDTO parentTagDto,
			List<AllianceTagDTO> childTagDtos);

	List<AllianceTagGroupDTO> getAllianceTagList(ListingLocator locator, Integer pageSize, Integer namespaceId,
			String ownerType, Long ownerId, Long type);

	ServiceAllianceDTO getServiceAlliance(GetServiceAllianceCommand cmd);

	String buildAllianceUrl(Integer namespaceId, ServiceAllianceInstanceConfig config, String pageRealDisplayType);
	
	ListOperateServicesResponse listOperateServices(ListOperateServicesCommand cmd);

	void updateOperateServices(UpdateOperateServicesCommand cmd);

	void updateOperateServiceOrders(UpdateOperateServiceOrdersCommand cmd);

	String processDetailUrl(Long serviceId, String serviceName, String ownerType, Long ownerId);

	boolean isAllianceOffLine(Integer namespaceId);

	String buildEntryUrl(Integer namespaceId, ServiceAllianceInstanceConfig config, String pageRealDisplayType);
}
