package com.everhomes.wifi;

import com.everhomes.server.schema.tables.pojos.EhWifiSettings;
import com.everhomes.util.StringHelper;

public class WifiSetting extends EhWifiSettings{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString(){
		return StringHelper.toJsonString(this);
	}

}
