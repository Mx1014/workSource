// @formatter:off
package com.everhomes.rest.techpark.punch;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>companyId：公司id</li>
 * <li>keyword: 员工关键字</li>
 * <li>startDay: 开始时间</li>
 * <li>endDay：结束时间</li>
 * <li>status: 处理状态</li>
 * <li>arriveTimeCompareFlag：开始工作时间符号，大于等于或者小于等于  参考{@link com.everhomes.rest.techpark.punch.TimeCompareFlag}</li>
 * <li>arriveTime：开始工作时间 </li>
 * <li>leaveTimeCompareFlag：结束工作时间符号，大于等于或者小于等于  参考{@link com.everhomes.rest.techpark.punch.TimeCompareFlag}</li>
 * <li>leaveTime：结束工作时间</li>
 * <li>workTimeCompareFlag：工作时长时间符号，大于等于或者小于等于  参考{@link com.everhomes.rest.techpark.punch.TimeCompareFlag}</li>
 * <li>workTime：每天工作时间 </li>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListPunchStatisticsCommand {
	@NotNull
	private Long    enterpriseId;
	private String keyword;
	private String startDay;
	private String endDay;
	private Byte arriveTimeCompareFlag;
	private String arriveTime;
	private Byte leaveTimeCompareFlag;
	private String leaveTime;
	private Byte workTimeCompareFlag;
	private String workTime;
	private Byte status;
	private Integer pageOffset;
	private Integer pageSize;
 
	
	
	public ListPunchStatisticsCommand() {
    } 
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getStartDay() {
		return startDay;
	}
	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Integer getPageOffset() {
		return pageOffset;
	}
	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	  
	public Byte getWorkTimeCompareFlag() {
		return workTimeCompareFlag;
	}
	public void setWorkTimeCompareFlag(Byte workTimeCompareFlag) {
		this.workTimeCompareFlag = workTimeCompareFlag;
	}
	public String getWorkTime() {
		return workTime;
	}
	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	public Byte getArriveTimeCompareFlag() {
		return arriveTimeCompareFlag;
	}
	public void setArriveTimeCompareFlag(Byte arriveTimeCompareFlag) {
		this.arriveTimeCompareFlag = arriveTimeCompareFlag;
	}
	public String getArriveTime() {
		return arriveTime;
	}
	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}
	public Byte getLeaveTimeCompareFlag() {
		return leaveTimeCompareFlag;
	}
	public void setLeaveTimeCompareFlag(Byte leaveTimeCompareFlag) {
		this.leaveTimeCompareFlag = leaveTimeCompareFlag;
	}
	public String getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}
	public Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	} 
}
