// @formatter:off
package com.everhomes.rest.techpark.punch;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：主键id</li>
 * <li>ownerType：organization/user</li>
 * <li>ownerId：id</li>
 * <li>name：名称</li>
 * <li>timeRuleId：时间规则id</li>
 * <li>locationRuleId：地点规则id</li>
 * <li>wifiRuleId：wifi规则id</li>
 * <li>workdayRuleId：排班规则id</li>
 * <li>timeRuleName：时间规则名称</li>
 * <li>locationRuleName：地点规则名称</li>
 * <li>wifiRuleName: 规则名称</li>
 * <li>workdayRuleName：排班规则名称</li>
 * </ul>
 */
public class PunchRuleDTO {
	@NotNull
	private Long id;
	private String ownerType;
	private Long ownerId;
	private String name;
	private Long timeRuleId;
	private Long locationRuleId;
	private Long wifiRuleId;
	private Long workdayRuleId;

	private String timeRuleName;
	private String locationRuleName;
	private String wifiRuleName;
	private String workdayRuleName;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTimeRuleId() {
		return timeRuleId;
	}

	public void setTimeRuleId(Long timeRuleId) {
		this.timeRuleId = timeRuleId;
	}

	public Long getLocationRuleId() {
		return locationRuleId;
	}

	public void setLocationRuleId(Long locationRuleId) {
		this.locationRuleId = locationRuleId;
	}

	public Long getWifiRuleId() {
		return wifiRuleId;
	}

	public void setWifiRuleId(Long wifiRuleId) {
		this.wifiRuleId = wifiRuleId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getWorkdayRuleId() {
		return workdayRuleId;
	}

	public void setWorkdayRuleId(Long workdayRuleId) {
		this.workdayRuleId = workdayRuleId;
	}

	public String getTimeRuleName() {
		return timeRuleName;
	}

	public void setTimeRuleName(String timeRuleName) {
		this.timeRuleName = timeRuleName;
	}

	public String getLocationRuleName() {
		return locationRuleName;
	}

	public void setLocationRuleName(String locationRuleName) {
		this.locationRuleName = locationRuleName;
	}

	public String getWifiRuleName() {
		return wifiRuleName;
	}

	public void setWifiRuleName(String wifiRuleName) {
		this.wifiRuleName = wifiRuleName;
	}

	public String getWorkdayRuleName() {
		return workdayRuleName;
	}

	public void setWorkdayRuleName(String workdayRuleName) {
		this.workdayRuleName = workdayRuleName;
	}
}
