// @formatter:off
package com.everhomes.techpark.punch;


import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>companyId：公司id</li>
 * <li>startEarlyTime：最早上班时间</li>
 * <li>startLateTime：最晚上班时间（如果是硬性工作时间，startLateTime = startEarlyTime）</li>
 * <li>endEarlyTime：最早下班班时间</li>
 * <li>endLateTime：最晚下班时间（如果是硬性工作时间，startLateTime = startEarlyTime）</li>
 * <li>workTime：每天工作时间 ,有由后台处理 (startLateTime - startEarlyTime)</li>
 * <li>locations：打卡地点的json数组</li>
 * </ul>
 */
public class AddPunchRuleCommand {
	
	@NotNull
	private Long     id;
	@NotNull
	private Long     EnterpriseId;
	
	@NotNull
	private Long      startEarlyTime;
	@NotNull
	private Long      startLateTime;
	@NotNull
	private Long      endEarlyTime;
	@NotNull
	private Long noonLeaveTime;
	@NotNull
	private Long afternoonArriveTime;
	@NotNull
	private Byte punchTimesPerDay;
	@NotNull
	@ItemType(PunchGeoPointDTO.class)
	private  List<PunchGeoPointDTO>  punchGeoPoints;
	 
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Long getEnterpriseId() {
		return EnterpriseId;
	}



	public void setEnterpriseId(Long enterpriseId) {
		EnterpriseId = enterpriseId;
	}



	public Long getStartEarlyTime() {
		return startEarlyTime;
	}



	public void setStartEarlyTime(Long startEarlyTime) {
		this.startEarlyTime = startEarlyTime;
	}



	public Long getStartLateTime() {
		return startLateTime;
	}



	public void setStartLateTime(Long startLateTime) {
		this.startLateTime = startLateTime;
	}



	public Long getEndEarlyTime() {
		return endEarlyTime;
	}



	public void setEndEarlyTime(Long endEarlyTime) {
		this.endEarlyTime = endEarlyTime;
	}



	public Long getNoonLeaveTime() {
		return noonLeaveTime;
	}



	public void setNoonLeaveTime(Long noonLeaveTime) {
		this.noonLeaveTime = noonLeaveTime;
	}



	public Long getAfternoonArriveTime() {
		return afternoonArriveTime;
	}



	public void setAfternoonArriveTime(Long afternoonArriveTime) {
		this.afternoonArriveTime = afternoonArriveTime;
	}



	public Byte getPunchTimesPerDay() {
		return punchTimesPerDay;
	}



	public void setPunchTimesPerDay(Byte punchTimesPerDay) {
		this.punchTimesPerDay = punchTimesPerDay;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public List<PunchGeoPointDTO> getPunchGeoPoints() {
		return punchGeoPoints;
	}



	public void setPunchGeoPoints(List<PunchGeoPointDTO> punchGeoPoints) {
		this.punchGeoPoints = punchGeoPoints;
	}
}
