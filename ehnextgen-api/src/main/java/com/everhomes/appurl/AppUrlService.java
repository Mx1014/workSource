package com.everhomes.appurl;


import com.everhomes.rest.appurl.GetAppInfoCommand;
import com.everhomes.rest.appurl.AppUrlDTO;
import com.everhomes.rest.appurl.CreateAppInfoCommand;
import com.everhomes.rest.appurl.UpdateAppInfoCommand;
import com.everhomes.rest.appurl.AppInfoByNamespaceIdDTO;

public interface AppUrlService {
	
	AppUrlDTO getAppInfo( GetAppInfoCommand cmd);
	
	/**
	 * 新增appURL信息方法
	 * @param cmd
	 */
	void  createAppInfo( CreateAppInfoCommand cmd);
	
	/**
	 * 更新appURL信息方法
	 * @param cmd
	 */
	void  updateAppInfo( UpdateAppInfoCommand cmd);
	
	/**
	 * 通过NamespaceId查询信息
	 * @param cmd
	 * @return
	 */
	AppInfoByNamespaceIdDTO getAppInfoByNamespaceId(GetAppInfoCommand cmd) ;

}
