package com.everhomes.appurl;


import com.everhomes.rest.appurl.GetAppInfoCommand;
import com.everhomes.rest.appurl.AppUrlDTO;

public interface AppUrlService {
	
	AppUrlDTO getAppInfo( GetAppInfoCommand cmd);

}
