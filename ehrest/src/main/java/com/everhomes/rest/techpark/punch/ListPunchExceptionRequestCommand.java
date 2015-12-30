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
 * <li>exceptionStatus: 处理状态</li>
 * <li>processCode: 处理结果</li>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListPunchExceptionRequestCommand {
	@NotNull
	private Long    enterpriseId;
	private String keyword;
	private String startDay;
	private String endDay;
	private Byte exceptionStatus;
	private Byte processCode;
	private Integer pageOffset;
	private Integer pageSize;
	
	public ListPunchExceptionRequestCommand() {
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
	public Byte getExceptionStatus() {
		return exceptionStatus;
	}
	public void setExceptionStatus(Byte exceptionStatus) {
		this.exceptionStatus = exceptionStatus;
	}
	public Byte getProcessCode() {
		return processCode;
	}
	public void setProcessCode(Byte processCode) {
		this.processCode = processCode;
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
	public Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
}
