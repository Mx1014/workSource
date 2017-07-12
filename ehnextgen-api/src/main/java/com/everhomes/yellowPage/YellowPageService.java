package com.everhomes.yellowPage;


import com.everhomes.rest.yellowPage.*;

import java.util.List;

public interface YellowPageService {

	YellowPageDTO getYellowPageDetail(GetYellowPageDetailCommand cmd);

	YellowPageListResponse getYellowPageList(GetYellowPageListCommand cmd);
 

	void addYellowPage(AddYellowPageCommand cmd);

	void deleteYellowPage(DeleteYellowPageCommand cmd);

	void updateYellowPage(UpdateYellowPageCommand cmd);

	YellowPageDTO getYellowPageTopic(GetYellowPageTopicCommand cmd);
	
	void updateServiceAllianceCategory(UpdateServiceAllianceCategoryCommand cmd);
	void deleteServiceAllianceCategory(DeleteServiceAllianceCategoryCommand cmd);

	ServiceAllianceDTO getServiceAllianceEnterpriseDetail(GetServiceAllianceEnterpriseDetailCommand cmd);
	ServiceAllianceDTO getServiceAlliance(GetServiceAllianceCommand cmd);
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


	List<JumpModuleDTO> listJumpModules();
	
	ListAttachmentsResponse listAttachments(ListAttachmentsCommand cmd);
	
	/**
	 * 设置显示或者隐藏服务联盟企业
	 */
	void updateServiceAllianceEnterpriseDisplayFlag(UpdateServiceAllianceEnterpriseDisplayFlagCommand cmd);

	/**
	 * 交换cmd中两个服务联盟的sortOrder
	 */
	ServiceAllianceListResponse updateServiceAllianceEnterpriseDefaultOrder(UpdateServiceAllianceEnterpriseDefaultOrderCommand cmd);
}
