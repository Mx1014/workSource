package com.everhomes.techpark.servicehotline;

import com.everhomes.rest.servicehotline.AddHotlineCommand;
import com.everhomes.rest.servicehotline.DeleteHotlineCommand;
import com.everhomes.rest.servicehotline.GetHotlineListCommand;
import com.everhomes.rest.servicehotline.GetHotlineListResponse;
import com.everhomes.rest.servicehotline.GetHotlineSubjectCommand;
import com.everhomes.rest.servicehotline.GetHotlineSubjectResponse;
import com.everhomes.rest.servicehotline.SetHotlineSubjectCommand;
import com.everhomes.rest.servicehotline.UpdateHotlineCommand;
import com.everhomes.rest.servicehotline.UpdateHotlinesCommand;

public interface HotlineService {

	GetHotlineSubjectResponse getHotlineSubject(GetHotlineSubjectCommand cmd);

	GetHotlineListResponse getHotlineList(GetHotlineListCommand cmd);

	void addHotline(AddHotlineCommand cmd);

	void deleteHotline(DeleteHotlineCommand cmd);

	void updateHotline(UpdateHotlineCommand cmd);
	
	void updateHotlines(UpdateHotlinesCommand cmd);

	void setHotlineSubject(SetHotlineSubjectCommand cmd);

	void updateHotlineOrder(UpdateHotlinesCommand cmd);


}
