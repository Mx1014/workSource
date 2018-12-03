package com.everhomes.rest.techpark.punch.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * 
 * <li>ownerType：查询类型organization/user</li>
 * <li>ownerId：查询对象id(如果是全部部门就是总公司id)</li>  
 * <li>userId：用户id如果不为空,则是查某个人的打卡记录</li>  
 * <li>includeSubDpt：是否包含子部门:0-不包含 1-包含(默认)</li>  
 * <li>monthReportId: 月报id</li> 
 * <li>userName：用户名搜索可为空</li>
 * <li>startDay: 开始时间</li>
 * <li>endDay：结束时间</li>
 * <li>exceptionStatus：异常状态搜索,全部则不传 0-正常 1-异常{@link com.everhomes.rest.techpark.punch.ExceptionStatus}</li>
 * <li>arriveTimeCompareFlag：开始工作时间符号，大于等于或者小于等于  参考{@link com.everhomes.rest.techpark.punch.TimeCompareFlag}</li>
 * <li>arriveTime：开始工作时间 </li>
 * <li>leaveTimeCompareFlag：结束工作时间符号，大于等于或者小于等于  参考{@link com.everhomes.rest.techpark.punch.TimeCompareFlag}</li>
 * <li>leaveTime：结束工作时间</li>
 * <li>workTimeCompareFlag：工作时长时间符号，大于等于或者小于等于  参考{@link com.everhomes.rest.techpark.punch.TimeCompareFlag}</li>
 * <li>workTime：每天工作时间 </li>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListPunchDetailsCommand {

	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	private Long userId;
	private Long monthReportId;
	private List<Long> departmentIds;

	private Byte includeSubDpt;
	private String userName;
	private Byte exceptionStatus;

	private Long startDay;
	private Long endDay;
	private Byte arriveTimeCompareFlag;
	private Long arriveTime;
	private Byte leaveTimeCompareFlag;
	private Long leaveTime;
	private Byte workTimeCompareFlag;
	private Long workTime;
	
	private Long pageAnchor;
	private Integer pageSize;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
  
	public Byte getExceptionStatus() {
		return exceptionStatus;
	}

	public void setExceptionStatus(Byte exceptionStatus) {
		this.exceptionStatus = exceptionStatus;
	}

	public Long getStartDay() {
		return startDay;
	}

	public void setStartDay(Long startDay) {
		this.startDay = startDay;
	}

	public Long getEndDay() {
		return endDay;
	}

	public void setEndDay(Long endDay) {
		this.endDay = endDay;
	}

	public Byte getArriveTimeCompareFlag() {
		return arriveTimeCompareFlag;
	}

	public void setArriveTimeCompareFlag(Byte arriveTimeCompareFlag) {
		this.arriveTimeCompareFlag = arriveTimeCompareFlag;
	}

	public Long getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(Long arriveTime) {
		this.arriveTime = arriveTime;
	}

	public Byte getLeaveTimeCompareFlag() {
		return leaveTimeCompareFlag;
	}

	public void setLeaveTimeCompareFlag(Byte leaveTimeCompareFlag) {
		this.leaveTimeCompareFlag = leaveTimeCompareFlag;
	}

	public Long getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(Long leaveTime) {
		this.leaveTime = leaveTime;
	}

	public Byte getWorkTimeCompareFlag() {
		return workTimeCompareFlag;
	}

	public void setWorkTimeCompareFlag(Byte workTimeCompareFlag) {
		this.workTimeCompareFlag = workTimeCompareFlag;
	}

	public Long getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Long workTime) {
		this.workTime = workTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Byte getIncludeSubDpt() {
		return includeSubDpt;
	}

	public void setIncludeSubDpt(Byte includeSubDpt) {
		this.includeSubDpt = includeSubDpt;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMonthReportId() {
		return monthReportId;
	}

	public void setMonthReportId(Long monthReportId) {
		this.monthReportId = monthReportId;
	}


	public List<Long> getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(List<Long> departmentIds) {
		this.departmentIds = departmentIds;
	}
}
