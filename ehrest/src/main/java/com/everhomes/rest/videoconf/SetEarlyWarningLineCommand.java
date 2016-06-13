package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>warningLine: 预警线</li>
 * <li>warningLineType: 预警线类型 0-源账号比率; 1-资源占用率; 2-25方仅视频比率; 3-25方支持电话比率; 4-100方仅视频比率; 5-100方支持电话比率; 
 * 								6-25方仅视频占用率; 7-25方支持电话占用率; 8-100方仅视频占用率; 9-100方支持电话占用率;
 * 								10-6方仅视频比率; 11-50方仅视频比率; 12-50支持电话比率; 13-6方仅视频占用率; 14-50方仅视频占用率; 15-50支持电话占用率;</li>
 * </ul>
 */
public class SetEarlyWarningLineCommand {
	
	private String warningLine;
	
	private Byte warningLineType;

	public String getWarningLine() {
		return warningLine;
	}

	public void setWarningLine(String warningLine) {
		this.warningLine = warningLine;
	}
	

	public Byte getWarningLineType() {
		return warningLineType;
	}

	public void setWarningLineType(Byte warningLineType) {
		this.warningLineType = warningLineType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
