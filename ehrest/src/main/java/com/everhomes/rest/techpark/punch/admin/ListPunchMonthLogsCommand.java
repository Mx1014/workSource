package com.everhomes.rest.techpark.punch.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 
 * <li>ownerType：查询类型organization/user</li>
 * <li>ownerId：查询对象id(如果是全部部门就是总公司id)</li> 
 * <li>punchMonth：传查询月的任意一天时间戳</li>
 * <li>userName：用户名搜索可为空</li>
 * <li>exceptionStatus：异常状态搜索,全部则不传 0-正常 1-异常{@link com.everhomes.rest.techpark.punch.ExceptionStatus}</li>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListPunchMonthLogsCommand {

	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	
	private Long punchMonth;
	private Long userName;
	private Byte exceptionStatus;

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

	public Long getPunchMonth() {
		return punchMonth;
	}

	public void setPunchMonth(Long punchMonth) {
		this.punchMonth = punchMonth;
	}

	public Long getUserName() {
		return userName;
	}

	public void setUserName(Long userName) {
		this.userName = userName;
	}

	public Byte getExceptionStatus() {
		return exceptionStatus;
	}

	public void setExceptionStatus(Byte exceptionStatus) {
		this.exceptionStatus = exceptionStatus;
	}
 

}
