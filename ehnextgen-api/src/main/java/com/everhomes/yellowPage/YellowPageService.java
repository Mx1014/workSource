package com.everhomes.yellowPage;



import com.everhomes.rest.yellowPage.AddYellowPageCommand;
import com.everhomes.rest.yellowPage.DeleteServiceAllianceCategoryCommand;
import com.everhomes.rest.yellowPage.DeleteServiceAllianceEnterpriseCommand;
import com.everhomes.rest.yellowPage.DeleteYellowPageCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceEnterpriseDetailCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceEnterpriseListCommand;
import com.everhomes.rest.yellowPage.GetYellowPageDetailCommand;
import com.everhomes.rest.yellowPage.GetYellowPageListCommand;
import com.everhomes.rest.yellowPage.GetYellowPageTopicCommand;
import com.everhomes.rest.yellowPage.ServiceAllianceDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceListResponse;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceCategoryCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceEnterpriseCommand;
import com.everhomes.rest.yellowPage.UpdateYellowPageCommand;
import com.everhomes.rest.yellowPage.YellowPageDTO;
import com.everhomes.rest.yellowPage.YellowPageListResponse;

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
}
