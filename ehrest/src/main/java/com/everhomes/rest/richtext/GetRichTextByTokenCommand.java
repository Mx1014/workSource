package com.everhomes.rest.richtext;

import com.everhomes.util.StringHelper;

/**
 * 
 * rtToken:获取富文本信息的token字段
 *
 */
public class GetRichTextByTokenCommand {
	
	private String rtToken;
	
	public String getRtToken() {
		return rtToken;
	}

	public void setRtToken(String rtToken) {
		this.rtToken = rtToken;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
