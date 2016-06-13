package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>monitoringPoints: 监测点  0-源账号比率; 1-资源占用率; 2-25方仅视频比率; 3-25方支持电话比率; 4-100方仅视频比率; 5-100方支持电话比率; 
 * 								6-25方仅视频占用率; 7-25方支持电话占用率; 8-100方仅视频占用率; 9-100方支持电话占用率</li>
 *  <li>ratio: 比率</li>
 *  <li>warningLine: 预警线</li>
 * </ul>
 *
 */
public class SourceVideoConfAccountStatistics {
	
	private Byte monitoringPoints;
	
	private Double ratio;
	
	private Double warningLine;

	public Byte getMonitoringPoints() {
		return monitoringPoints;
	}

	public void setMonitoringPoints(Byte monitoringPoints) {
		this.monitoringPoints = monitoringPoints;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}
	
	public Double getWarningLine() {
		return warningLine;
	}

	public void setWarningLine(Double warningLine) {
		this.warningLine = warningLine;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
