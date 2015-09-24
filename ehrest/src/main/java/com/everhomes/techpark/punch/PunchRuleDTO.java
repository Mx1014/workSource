// @formatter:off
package com.everhomes.techpark.punch;

import java.sql.Time;
import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：主键id</li>
 * <li>companyId：公司id</li>
 * <li>startEarlyTime：最早上班时间</li>
 * <li>startLateTime：最晚上班时间（如果是硬性工作时间，startLateTime = startEarlyTime）</li>
 * <li>endEarlyTime：最早下班班时间</li>
 * <li>endLateTime：最晚下班时间（如果是硬性工作时间，startLateTime = startEarlyTime）</li>
 * <li>workTime：每天工作时间 ,有由后台处理 (startLateTime - startEarlyTime)</li>
 * <li>createUid：创建人id</li>
 * <li>createTime：创建时间</li>
 * <li>updateUid：更新人id</li>
 * <li>updateTime：更新时间</li>
 * </ul>
 */
public class PunchRuleDTO {
	@NotNull
	private Long     id;
	@NotNull
	private Long     companyId;
	
	@NotNull
	private String      startEarlyTime;
	@NotNull
	private String      startLateTime;
	@NotNull
	private String      endEarlyTime;
	@NotNull
	private String      endLateTime;
	private Time workTime;
	private Long     createUid;
	private Timestamp createTime;
	private Long     updateUid;
	private Timestamp updateTime;
	
	@NotNull
	private String      locations;
	
	public PunchRuleDTO() {
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getStartEarlyTime() {
		return startEarlyTime;
	}
	public void setStartEarlyTime(String startEarlyTime) {
		this.startEarlyTime = startEarlyTime;
	}
	public String getStartLateTime() {
		return startLateTime;
	}
	public void setStartLateTime(String startLateTime) {
		this.startLateTime = startLateTime;
	}
	public String getEndEarlyTime() {
		return endEarlyTime;
	}
	public void setEndEarlyTime(String endEarlyTime) {
		this.endEarlyTime = endEarlyTime;
	}
	public String getEndLateTime() {
		return endLateTime;
	}
	public void setEndLateTime(String endLateTime) {
		this.endLateTime = endLateTime;
	}
	public Time getWorkTime() {
		return workTime;
	}
	public void setWorkTime(Time workTime) {
		this.workTime = workTime;
	}
	public Long getCreateUid() {
		return createUid;
	}
	public void setCreateUid(Long createUid) {
		this.createUid = createUid;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Long getUpdateUid() {
		return updateUid;
	}
	public void setUpdateUid(Long updateUid) {
		this.updateUid = updateUid;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public String getLocations() {
		return locations;
	}
	public void setLocations(String locations) {
		this.locations = locations;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
