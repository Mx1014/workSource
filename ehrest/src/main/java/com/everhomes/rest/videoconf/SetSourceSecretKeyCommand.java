package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 
 * secretKey: 视频会议第三方分配给左邻的密钥
 *
 */
public class SetSourceSecretKeyCommand {
	
	private String secretKey;

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
