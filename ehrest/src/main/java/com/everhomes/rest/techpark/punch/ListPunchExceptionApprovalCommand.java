// @formatter:off
package com.everhomes.rest.techpark.punch;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId：查询用户id</li> 
 * <li>companyId：公司id</li> 
 * <li>processCode: 处理结果</li>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListPunchExceptionApprovalCommand {
	@NotNull

	private Long    userId;
	private Long    enterpriseId;
	private String punchDate;
	private Integer pageOffset;
	private Integer pageSize;
	
	public ListPunchExceptionApprovalCommand() {
    } 
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getPunchDate() {
		return punchDate;
	}
	public void setPunchDate(String punchDate) {
		this.punchDate = punchDate;
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
}
