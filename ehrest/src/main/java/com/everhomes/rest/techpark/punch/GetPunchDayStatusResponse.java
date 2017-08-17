package com.everhomes.rest.techpark.punch;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;


/**
* <ul>  
* <li>expiryTime：long 有效期时间戳(过期重新调接口)</li> 
* <li>punchTimesPerDay：一天要打几次卡(总时间段*2)</li> 
* <li>punchType：下次打卡是上班还是下班 0-上班 1-下班 2-不打卡 参考 {@link PunchType}</li> 
* <li>punchIntevalNo：第几个打卡时间段</li> 
* <li>punchNormalTime：(下次打卡)正确的打卡时间--(如果是上班打卡,在此之前不能打卡,如果是下班打卡,在此之前是早退)</li> 
* <li>punchLogs：打卡记录列表 参考{@link PunchLogDTO}</li> 
* </ul>
*/
public class GetPunchDayStatusResponse {
	private Long expiryTime;
	private Byte punchTimesPerDay;
	private Byte punchType;
	private Integer punchIntevalNo;
	private Long punchNormalTime;
	@ItemType(PunchLogDTO.class)
	private List<PunchLogDTO> punchLogs;
	
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

	public Integer getPunchIntevalNo() {
		return punchIntevalNo;
	}

	public void setPunchIntevalNo(Integer punchIntevalNo) {
		this.punchIntevalNo = punchIntevalNo;
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

	public List<PunchLogDTO> getPunchLogs() {
		return punchLogs;
	}

	public void setPunchLogs(List<PunchLogDTO> punchLogs) {
		this.punchLogs = punchLogs;
	}
}
