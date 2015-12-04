package com.everhomes.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>warningLine: 预警线</li>
 * <li>confType: 会议类型 0-25方仅视频; 1-25方支持电话; 2-100方仅视频; 3-100方支持电话</li>
 * </ul>
 */
public class SetEarlyWarningLineCommand {
	
	private String warningLine;
	
	private Byte confType;

	public String getWarningLine() {
		return warningLine;
	}

	public void setWarningLine(String warningLine) {
		this.warningLine = warningLine;
	}
	
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
