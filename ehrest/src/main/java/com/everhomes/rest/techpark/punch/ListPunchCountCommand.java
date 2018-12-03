// @formatter:off
package com.everhomes.rest.techpark.punch;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * 
 * <li>ownerType：查询类型organization/user</li>
 * <li>ownerId：查询对象id(如果是全部部门就是总公司id)</li>  
 * <li>includeSubDpt：是否包含子部门:0-不包含 1-包含(默认)</li>  
 * <li>userName：用户名搜索可为空</li>
 * <li>departmentIds：部门id列表</li>
 * <li>month: 查询月份,比如201608</li>
 * <li>startDay: 查询开始时间</li>
 * <li>endDay：查询结束时间</li>
 * <li>monthReportId: 月报id</li>
 * <li>exceptionStatus：异常状态搜索,全部则不传 0-正常 1-异常{@link com.everhomes.rest.techpark.punch.ExceptionStatus}</li> 
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListPunchCountCommand {

	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;

	private Byte includeSubDpt;
	private String month;
	private Long startDay;
	private Long endDay;
	private Byte exceptionStatus;

	private String userName;
	private List<Long> departmentIds;

	private Long pageAnchor;
	private Integer pageSize;

	private Long monthReportId;
	
	public ListPunchCountCommand() {
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
	public Byte getExceptionStatus() {
		return exceptionStatus;
	}
	public void setExceptionStatus(Byte exceptionStatus) {
		this.exceptionStatus = exceptionStatus;
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
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
