package com.everhomes.rest.techpark.punch;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.approval.ExceptionRequestDTO;
import com.everhomes.util.StringHelper;


/**
* <ul>  
* <li>expiryTime：long 有效期时间戳(过期重新调接口)</li> 
* <li>punchDayType：工作日/休息日/节假日  参考{@link com.everhomes.rest.techpark.punch.PunchDayType}</li>
* <li>punchDate：long  这时候打卡算作哪天</li>
* <li>punchTimesPerDay：一天要打几次卡(总时间段*2)</li>
* <li>punchType：下次打卡是上班还是下班 0-上班 1-下班 2-不在上班时间 3-今天休息 4-今天没排班 5-今天打完卡了 参考 {@link com.everhomes.rest.techpark.punch.PunchType}</li>
* <li>punchIntervalNo：第几个打卡时间段</li> 
* <li>punchNormalTime：(下次打卡)正确的打卡时间--(如果是上班打卡,这个字段没用 ; 如果是下班打卡,在此之前是早退)</li>
 * <li>intervals：打卡班次列表 参考{@link com.everhomes.rest.techpark.punch.PunchIntevalLogDTO}</li>
 * <li>statusList：班次的打卡状态列表多个状态用/分隔:比如"0" "0/2/1"</li>
 * <li>clockStatus：下次打卡的打卡状态 参考{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>requestDTOs：当日申请列表 参考{@link com.everhomes.rest.approval.ExceptionRequestDTO}</li>
 * <li>goOutPunchLogs：外出打卡列表 参考{@link com.everhomes.rest.techpark.punch.GoOutPunchLogDTO}</li>
* <li>enterpriseId：公司id</li>
* <li>detailId：人员detail id</li> 
* <li>userId：人员uid</li> 
* </ul>
*/
public class GetPunchDayStatusResponse {
	private Long expiryTime;
	private Long punchDate;
	private Byte punchTimesPerDay;
	private Byte punchType;
	private Integer punchIntervalNo;
	private Long punchNormalTime;
	private Byte clockStatus;
    private Byte punchDayType;

	@ItemType(PunchIntevalLogDTO.class)
	private List<PunchIntevalLogDTO> intervals;
	private String statusList;
	@ItemType(ExceptionRequestDTO.class)
	private List<ExceptionRequestDTO> requestDTOs;
	@ItemType(GoOutPunchLogDTO.class)
	private List<GoOutPunchLogDTO> goOutPunchLogs;
    private Long enterpriseId;
	private Long detailId;
	private Long userId;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Byte getClockStatus() {
		return clockStatus;
	}

	public void setClockStatus(Byte clockStatus) {
		this.clockStatus = clockStatus;
	}

	public Byte getPunchDayType() {
		return punchDayType;
	}

	public void setPunchDayType(Byte punchDayType) {
		this.punchDayType = punchDayType;
	}

	public List<ExceptionRequestDTO> getRequestDTOs() {
		return requestDTOs;
	}

	public void setRequestDTOs(List<ExceptionRequestDTO> requestDTOs) {
		this.requestDTOs = requestDTOs;
	}

	public List<GoOutPunchLogDTO> getGoOutPunchLogs() {
		return goOutPunchLogs;
	}

	public void setGoOutPunchLogs(List<GoOutPunchLogDTO> goOutPunchLogs) {
		this.goOutPunchLogs = goOutPunchLogs;
	}
}
