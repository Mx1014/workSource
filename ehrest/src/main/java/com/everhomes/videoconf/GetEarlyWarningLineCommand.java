package com.everhomes.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 
 * confType: 会议类型 0-25方仅视频; 1-25方支持电话; 2-100方仅视频; 3-100方支持电话
 *
 */
public class GetEarlyWarningLineCommand {

	private Byte confType;

	public Byte getConfType() {
		return confType;
	}

	public void setConfType(Byte confType) {
		this.confType = confType;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
