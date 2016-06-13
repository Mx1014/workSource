package com.everhomes.rest.techpark.company;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>ownerId：companyId</li>
 * <li>keyword:	关键字</li>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListGroupContactsCommand {
	@NotNull
	private Long ownerId;
	private String keyword;
	private Integer pageOffset;
	private Integer pageSize;
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
	

}
