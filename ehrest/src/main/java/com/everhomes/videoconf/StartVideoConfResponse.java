package com.everhomes.videoconf;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>startConfUrl: 开始会议地址</li>
 * </ul>
 *
 */
public class StartVideoConfResponse {

	private String startConfUrl;
	
	public String getStartConfUrl() {
		return startConfUrl;
	}

	public void setStartConfUrl(String startConfUrl) {
		this.startConfUrl = startConfUrl;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
