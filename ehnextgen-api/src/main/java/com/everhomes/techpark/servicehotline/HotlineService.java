package com.everhomes.techpark.servicehotline;

import com.everhomes.rest.servicehotline.*;

public interface HotlineService {

	GetHotlineSubjectResponse getHotlineSubject(GetHotlineSubjectCommand cmd);

	GetHotlineListResponse getHotlineList(GetHotlineListCommand cmd);

	void addHotline(AddHotlineCommand cmd);

	void deleteHotline(DeleteHotlineCommand cmd);

	void updateHotline(UpdateHotlineCommand cmd);
	
	void updateHotlines(UpdateHotlinesCommand cmd);

	void setHotlineSubject(SetHotlineSubjectCommand cmd);

	void updateHotlineOrder(UpdateHotlinesCommand cmd);

	GetUserInfoByIdResponse getUserInfoById(GetUserInfoByIdCommand cmd);


}
