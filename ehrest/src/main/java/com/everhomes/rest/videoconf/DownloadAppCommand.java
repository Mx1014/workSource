package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 
 * appType: app类型，0：ios  1：andriod
 *
 */
public class DownloadAppCommand {
	
	private Byte appType;

	
	public Byte getAppType() {
		return appType;
	}


	public void setAppType(Byte appType) {
		this.appType = appType;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
