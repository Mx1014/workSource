package com.everhomes.pushmessagelog;

import com.everhomes.rest.message.PushMessageToAdminAndBusinessContactsCommand;
import com.everhomes.rest.organization.pm.SendNoticeCommand;
import com.everhomes.rest.pushmessagelog.PushMessageListCommand;
import com.everhomes.rest.pushmessagelog.PushMessageLogReturnDTO;


/**
 * 一键推送消息记录Service
 * @author huanglm
 *
 */
public interface PushMessageLogService {
	
	/**
	 * 通过域空间、操作者ID来查询推送记录
	 * @param cmd
	 * @return
	 */
	PushMessageLogReturnDTO listPushMessageLogByNamespaceIdAndOperator(PushMessageListCommand cmd);
	
	/**
	 * 通过ID来查询推送记录
	 * @param id
	 * @return
	 */
	PushMessageLog getPushMessageLogById(Integer id);
	
	
	/**
	 * 创建推送消息记录
	 * @param bo	PushMessageLog
	 */
	Long crteatePushMessageLog(PushMessageLog bo);
	
	/**
	 * 修改推送消息记录
	 * @param bo	PushMessageLog
	 */
	void updatePushMessageLog(PushMessageLog bo);
	
	/**
	 * 删除推送消息记录
	 * @param bo	PushMessageLog
	 */
	void deletePushMessageLog(PushMessageLog bo);
	

	/**
	 * 更新推送状态
	 * @param id
	 * @param status
	 */
	 void updatePushStatus(Long id , Byte status);
	 
	 /**
	  * 创建推送记录
	  * @param cmd
	  * @param pushTypeCode
	  * @return
	  */
	 Long createfromSendNotice(SendNoticeCommand cmd ,Byte pushTypeCode);
	 
	 /**
		 * 短信方式推送时创建消息记录
		 * @param cmd
		 * @return
		 */
	 Long createfromSendNotice(PushMessageToAdminAndBusinessContactsCommand cmd ,Byte pushTypeCode);
}
