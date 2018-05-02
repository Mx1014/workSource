package com.everhomes.techpark.servicehotline;

import javax.servlet.http.HttpServletResponse;

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
	
	/**
	 * 获取专属客服的会话列表
	 */
	GetChatGroupListResponse getChatGroupList(GetChatGroupListCommand cmd);

	/**
	 * 获取专属客服的聊天记录
	 */
	GetChatRecordListResponse getChatRecordList(GetChatRecordListCommand cmd);
	
	
	/**
	 * 导出专属客服的单个会话的聊天记录
	 */
	public void exportChatRecordList(GetChatRecordListCommand cmd, HttpServletResponse httpResponse);
	
	/**
	 * 根据条件导出多个会话聊天记录
	 */
	public void exportMultiChatRecordList(GetChatGroupListCommand cmd, HttpServletResponse httpResponse);

}
