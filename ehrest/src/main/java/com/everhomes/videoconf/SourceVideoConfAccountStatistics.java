package com.everhomes.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>monitoringPoints: 监测点</li>
 *  <li>ratio: 比率</li>
 * </ul>
 *
 */
public class SourceVideoConfAccountStatistics {
	
	private String monitoringPoints;
	
	private Double ratio;

	public String getMonitoringPoints() {
		return monitoringPoints;
	}

	public void setMonitoringPoints(String monitoringPoints) {
		this.monitoringPoints = monitoringPoints;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
