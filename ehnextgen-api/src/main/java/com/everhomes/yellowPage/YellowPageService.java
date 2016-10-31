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
     * 获取服务联盟父分类
     * (用于客户端在主页签上显示服务联盟获取parentId及displayMode使用)
     */
    List<ServiceAllianceCategoryDTO> getParentServiceAllianceCategory(ListServiceAllianceCategoriesCommand cmd);
}
