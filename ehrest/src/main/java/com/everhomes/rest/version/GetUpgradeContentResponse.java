// @formatter:off
package com.everhomes.rest.version;

import java.sql.Timestamp;

/**
 * 
 * <ul>
 * <li>appName: </li>
 * <li>targetVersion: </li>
 * <li>publishTime: </li>
 * <li>upgradeDescription: </li>
 * </ul>
 */
public class GetUpgradeContentResponse {
	private String appName;
	private String targetVersion;
	private Timestamp publishTime;
	private String upgradeDescription;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getTargetVersion() {
		return targetVersion;
	}

	public void setTargetVersion(String targetVersion) {
		this.targetVersion = targetVersion;
	}

	public Timestamp getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}

	public String getUpgradeDescription() {
		return upgradeDescription;
	}

	public void setUpgradeDescription(String upgradeDescription) {
		this.upgradeDescription = upgradeDescription;
	}

}
