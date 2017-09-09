package com.everhomes.rest.techpark.punch;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;


/**
* <ul>  
* <li>expiryTime：long 有效期时间戳(过期重新调接口)</li> 
* <li>punchDate：long  这时候打卡算作哪天</li>
* <li>punchTimesPerDay：一天要打几次卡(总时间段*2)</li>
* <li>punchType：下次打卡是上班还是下班 0-上班 1-下班 2-不在上班时间 3-今天休息 4-今天没排班 5-今天打完卡了 参考 {@link com.everhomes.rest.techpark.punch.PunchType}</li>
* <li>punchIntervalNo：第几个打卡时间段</li> 
* <li>punchNormalTime：(下次打卡)正确的打卡时间--(如果是上班打卡,这个字段没用 ; 如果是下班打卡,在此之前是早退)</li>
 * <li>intervals：打卡班次列表 参考{@link com.everhomes.rest.techpark.punch.PunchIntevalLogDTO}</li>
 * <li>statusList：班次的打卡状态列表多个状态用/分隔:比如"0" "0/2/1"</li>
* </ul>
*/
public class GetPunchDayStatusResponse {
	private Long expiryTime;
	private Long punchDate;
	private Byte punchTimesPerDay;
	private Byte punchType;
	private Integer punchIntervalNo;
	private Long punchNormalTime;

	@ItemType(PunchIntevalLogDTO.class)
	private List<PunchIntevalLogDTO> intervals;
	private String statusList;
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Byte getPunchType() {
		return punchType;
	}

	public void setPunchType(Byte punchType) {
		this.punchType = punchType;
	}

	public Long getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Long expiryTime) {
		this.expiryTime = expiryTime;
	}

	public Byte getPunchTimesPerDay() {
		return punchTimesPerDay;
	}

	public void setPunchTimesPerDay(Byte punchTimesPerDay) {
		this.punchTimesPerDay = punchTimesPerDay;
	}

	public Long getPunchNormalTime() {
		return punchNormalTime;
	}

	public void setPunchNormalTime(Long punchNormalTime) {
		this.punchNormalTime = punchNormalTime;
	}

	public String getStatusList() {
		return statusList;
	}

	public void setStatusList(String statusList) {
		this.statusList = statusList;
	}

	public List<PunchIntevalLogDTO> getIntervals() {
		return intervals;
	}

	public void setIntervals(List<PunchIntevalLogDTO> intervals) {
		this.intervals = intervals;
	}

	public Integer getPunchIntervalNo() {
		return punchIntervalNo;
	}

	public void setPunchIntervalNo(Integer punchIntervalNo) {
		this.punchIntervalNo = punchIntervalNo;
	}

	public Long getPunchDate() {
		return punchDate;
	}

	public void setPunchDate(Long punchDate) {
		this.punchDate = punchDate;
	}
}
