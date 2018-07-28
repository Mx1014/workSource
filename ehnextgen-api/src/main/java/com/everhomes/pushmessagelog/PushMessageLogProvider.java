package com.everhomes.pushmessagelog;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;


/**
 * 一键推送消息记录provider
 * @author huanglm
 *
 */
public interface PushMessageLogProvider {
	
	/**
	 * 通过域空间、操作者ID来查询推送记录
	 * @param namespaceId
	 * @param operatorId
	 * @param pageSize
	 * @param locator
	 * @return
	 */
	List<PushMessageLog> listPushMessageLogByNamespaceIdAndOperator(Integer namespaceId,Integer operatorId,
													Integer  pageSize,CrossShardListingLocator locator);
	

	/**
	 * 通过ID来查询推送记录
	 * @param id
	 * @return
	 */
	PushMessageLog getPushMessageLogById(Integer id);
	
	/**
	 * 创建推送消息记
	 * @param bo	PushMessageLog
	 */
	Long crteatePushMessageLog(PushMessageLog bo);
	
	/**
	 * 修改推送消息记
	 * @param bo	PushMessageLog
	 */
	void updatePushMessageLog(PushMessageLog bo);
	
	/**
	 * 删除推送消息记
	 * @param bo	PushMessageLog
	 */
	void deletePushMessageLog(PushMessageLog bo);
	

}
