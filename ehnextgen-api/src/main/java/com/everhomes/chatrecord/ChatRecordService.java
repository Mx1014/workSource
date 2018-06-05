package com.everhomes.chatrecord;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.servicehotline.GetChatGroupListCommand;
import com.everhomes.rest.servicehotline.GetChatGroupListResponse;
import com.everhomes.rest.servicehotline.GetChatRecordListCommand;
import com.everhomes.rest.servicehotline.GetChatRecordListResponse;

public interface ChatRecordService {
	
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
