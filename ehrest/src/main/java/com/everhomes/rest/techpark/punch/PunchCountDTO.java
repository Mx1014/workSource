package com.everhomes.rest.techpark.punch;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;



/**
 * <ul>
 * <li>userId：用户id</li>
 * <li>userName：名称</li>
 * <li>punchMonth：打卡月份</li>
 * <li>token：联系电话</li>
 * <li>deptId：部门id</li>
 * <li>deptName：部门</li>
 * <li>punchOrgName：所属规则</li>
 * <li>workDayCount:应上班天数</li>
 * <li>exceptionDayCount:异常天数</li>
 * <li>workCount:正常天数</li>
 * <li>belateCount：迟到天数</li>
 * <li>leaveEarlyCount：早退天数</li>
 * <li>unPunchCount：未打卡天数</li>
 * <li>blandleCount：迟到且早退天数</li>
 * <li>extTotal: 请假总时长，单位天</li>
 * <li>extTotalDisplay: 请假总时长格式化显示，如3.0天1.0小时</li>
 * <li>exts：附加请假exts {@link com.everhomes.rest.techpark.punch.ExtDTO}</li>
 * <li>userStatus：用户状态{@link com.everhomes.rest.techpark.punch.PunchUserStatus} </li> 
 * <li>annualLeaveBalance：年假余额</li>
 * <li>overtimeCompensationBalance：调休余额</li>
 * <li>deviceChangeCounts：设备异常数量</li>
 * <li>exceptionRequestCounts：异常申请数</li>
 * <li>belateTime：迟到时长</li>
 * <li>leaveEarlyTime：早退时长</li>
 * <li>forgotPunchCountOnDuty: 上班缺卡次数</li>
 * <li>forgotPunchCountOffDuty: 下班缺卡次数</li>
 * <li>forgotCount：缺卡次数</li>
 * <li>overtimeTotalWorkdayDisplay: 工作日加班时长，格式：xx小时xx分钟</li>
 * <li>overtimeTotalRestdayDisplay: 休息日加班时长，格式：xx小时xx分钟</li>
 * <li>overtimeTotalLegalHolidayDisplay: 节假日加班时长，格式：xx小时xx分钟</li>
 * <li>statusList：每天状态列表 参考{@link com.everhomes.rest.techpark.punch.DayStatusDTO}</li>
 * </ul>
 */
public class PunchCountDTO {
	private Long userId;
	private Long detailId;
	private String userName;
	private String punchMonth;
	private String punchOrgName;
	private String token;
	private Long deptId;
	private String deptName;
    private Integer workDayCount;
    private Double workCount;
    private Integer belateCount;
    private Integer leaveEarlyCount;
    private Double unpunchCount;
    private Integer blandleCount;
    private Double absenceCount;
    private Double sickCount;
    private Double exchangeCount;
    private Double outworkCount;
    private Double overTimeSum;
	private java.lang.Byte     punchTimesPerDay;
	private String userEnterpriseGroup;
	@ItemType(AbsenceTimeDTO.class)
	private List<AbsenceTimeDTO> absenceTimeList;
	private Double extTotal;
	private String extTotalDisplay;
	@ItemType(ExtDTO.class)
	private List<ExtDTO> exts;
	private Byte userStatus;
	private Integer exceptionDayCount;
    private Double annualLeaveBalance;
    private Double overtimeCompensationBalance;
    private Integer deviceChangeCounts;
    private Integer exceptionRequestCounts;
    private String belateTime;
    private String leaveEarlyTime;
	private Integer forgotCount;
	private Integer forgotPunchCountOffDuty;
	private Integer forgotPunchCountOnDuty;
	private String overtimeTotalWorkdayDisplay;
	private String overtimeTotalRestdayDisplay;
	private String overtimeTotalLegalHolidayDisplay;
    private List<DayStatusDTO> statusList;

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public Integer getExceptionDayCount() {
		return exceptionDayCount;
	}

	public void setExceptionDayCount(Integer exceptionDayCount) {
		this.exceptionDayCount = exceptionDayCount;
	}

    public List<AbsenceTimeDTO> getAbsenceTimeList() {
		return absenceTimeList;
	}
	public void setAbsenceTimeList(List<AbsenceTimeDTO> absenceTimeList) {
		this.absenceTimeList = absenceTimeList;
	}
	public Integer getWorkDayCount() {
		return workDayCount;
	}
	public void setWorkDayCount(Integer workDayCount) {
		this.workDayCount = workDayCount;
	} 
	public Integer getBelateCount() {
		return belateCount;
	}
	public void setBelateCount(Integer belateCount) {
		this.belateCount = belateCount;
	}
	public Integer getLeaveEarlyCount() {
		return leaveEarlyCount;
	}
	public void setLeaveEarlyCount(Integer leaveEarlyCount) {
		this.leaveEarlyCount = leaveEarlyCount;
	}
	public Double getUnpunchCount() {
		return unpunchCount;
	}
	public void setUnpunchCount(Double unPunchCount) {
		this.unpunchCount = unPunchCount;
	}
	public Integer getBlandleCount() {
		return blandleCount;
	}
	public void setBlandleCount(Integer blandleCount) {
		this.blandleCount = blandleCount;
	} 
	
	public Double getAbsenceCount() {
		return absenceCount;
	}
	public void setAbsenceCount(Double absenceCount) {
		this.absenceCount = absenceCount;
	}
	public Double getSickCount() {
		return sickCount;
	}
	public void setSickCount(Double sickCount) {
		this.sickCount = sickCount;
	}
	public Double getExchangeCount() {
		return exchangeCount;
	}
	public void setExchangeCount(Double exchangeCount) {
		this.exchangeCount = exchangeCount;
	}
	public Double getOutworkCount() {
		return outworkCount;
	}
	public void setOutworkCount(Double outworkCount) {
		this.outworkCount = outworkCount;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    } 
	public java.lang.Byte getPunchTimesPerDay() {
		return punchTimesPerDay;
	}
	public void setPunchTimesPerDay(java.lang.Byte punchTimesPerDay) {
		this.punchTimesPerDay = punchTimesPerDay;
	}
	public String getUserEnterpriseGroup() {
		return userEnterpriseGroup;
	}
	public void setUserEnterpriseGroup(String userEnterpriseGroup) {
		this.userEnterpriseGroup = userEnterpriseGroup;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Double getWorkCount() {
		return workCount;
	}
	public void setWorkCount(Double workCount) {
		this.workCount = workCount;
	}
	public Double getOverTimeSum() {
		return overTimeSum;
	}
	public void setOverTimeSum(Double overTimeSum) {
		this.overTimeSum = overTimeSum;
	}
	public List<ExtDTO> getExts() {
		return exts;
	}
	public void setExts(List<ExtDTO> exts) {
		this.exts = exts;
	}
	public Byte getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(Byte userStatus) {
		this.userStatus = userStatus;
	}
	public String getPunchOrgName() {
		return punchOrgName;
	}
	public void setPunchOrgName(String punchOrgName) {
		this.punchOrgName = punchOrgName;
	}
	public String getPunchMonth() {
		return punchMonth;
	}
	public void setPunchMonth(String punchMonth) {
		this.punchMonth = punchMonth;
	}

	public Double getAnnualLeaveBalance() {
		return annualLeaveBalance;
	}

	public void setAnnualLeaveBalance(Double annualLeaveBalance) {
		this.annualLeaveBalance = annualLeaveBalance;
	}

	public Double getOvertimeCompensationBalance() {
		return overtimeCompensationBalance;
	}

	public void setOvertimeCompensationBalance(Double overtimeCompensationBalance) {
		this.overtimeCompensationBalance = overtimeCompensationBalance;
	}

	public Integer getDeviceChangeCounts() {
		return deviceChangeCounts;
	}

	public void setDeviceChangeCounts(Integer deviceChangeCounts) {
		this.deviceChangeCounts = deviceChangeCounts;
	}

	public Integer getExceptionRequestCounts() {
		return exceptionRequestCounts;
	}

	public void setExceptionRequestCounts(Integer exceptionRequestCounts) {
		this.exceptionRequestCounts = exceptionRequestCounts;
	}

	public String getBelateTime() {
		return belateTime;
	}

	public void setBelateTime(String belateTime) {
		this.belateTime = belateTime;
	}

	public String getLeaveEarlyTime() {
		return leaveEarlyTime;
	}

	public void setLeaveEarlyTime(String leaveEarlyTime) {
		this.leaveEarlyTime = leaveEarlyTime;
	}

	public Integer getForgotCount() {
		return forgotCount;
	}

	public void setForgotCount(Integer forgotCount) {
		this.forgotCount = forgotCount;
	}

	public List<DayStatusDTO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<DayStatusDTO> statusList) {
		this.statusList = statusList;
	}

	public Double getExtTotal() {
		return extTotal;
	}

	public void setExtTotal(Double extTotal) {
		this.extTotal = extTotal;
	}

	public String getExtTotalDisplay() {
		return extTotalDisplay;
	}

	public void setExtTotalDisplay(String extTotalDisplay) {
		this.extTotalDisplay = extTotalDisplay;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getOvertimeTotalWorkdayDisplay() {
		return overtimeTotalWorkdayDisplay;
	}

	public void setOvertimeTotalWorkdayDisplay(String overtimeTotalWorkdayDisplay) {
		this.overtimeTotalWorkdayDisplay = overtimeTotalWorkdayDisplay;
	}

	public String getOvertimeTotalRestdayDisplay() {
		return overtimeTotalRestdayDisplay;
	}

	public void setOvertimeTotalRestdayDisplay(String overtimeTotalRestdayDisplay) {
		this.overtimeTotalRestdayDisplay = overtimeTotalRestdayDisplay;
	}

	public String getOvertimeTotalLegalHolidayDisplay() {
		return overtimeTotalLegalHolidayDisplay;
	}

	public void setOvertimeTotalLegalHolidayDisplay(String overtimeTotalLegalHolidayDisplay) {
		this.overtimeTotalLegalHolidayDisplay = overtimeTotalLegalHolidayDisplay;
	}

	public Integer getForgotPunchCountOffDuty() {
		return forgotPunchCountOffDuty;
	}

	public void setForgotPunchCountOffDuty(Integer forgotPunchCountOffDuty) {
		this.forgotPunchCountOffDuty = forgotPunchCountOffDuty;
	}

	public Integer getForgotPunchCountOnDuty() {
		return forgotPunchCountOnDuty;
	}

	public void setForgotPunchCountOnDuty(Integer forgotPunchCountOnDuty) {
		this.forgotPunchCountOnDuty = forgotPunchCountOnDuty;
	}
}
