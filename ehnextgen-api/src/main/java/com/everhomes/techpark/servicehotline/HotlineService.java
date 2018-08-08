package com.everhomes.techpark.servicehotline;

import com.everhomes.chatrecord.ChatRecordService;
import com.everhomes.rest.servicehotline.*;

public interface HotlineService extends ChatRecordService{

	GetHotlineSubjectResponse getHotlineSubject(GetHotlineSubjectCommand cmd);

	// 前端或者app使用
	GetHotlineListResponse getHotlineList(GetHotlineListCommand cmd);
	
	//后台使用
	GetHotlineListResponse getHotlineListAdmin(GetHotlineListCommand cmd);

	void addHotline(AddHotlineCommand cmd);

	void deleteHotline(DeleteHotlineCommand cmd);

	void updateHotline(UpdateHotlineCommand cmd);
	
	void updateHotlines(UpdateHotlinesCommand cmd);

	void setHotlineSubject(SetHotlineSubjectCommand cmd);

	void updateHotlineOrder(UpdateHotlinesCommand cmd);

	GetUserInfoByIdResponse getUserInfoById(GetUserInfoByIdCommand cmd);
}
