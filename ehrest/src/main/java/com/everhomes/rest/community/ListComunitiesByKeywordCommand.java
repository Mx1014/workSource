package com.everhomes.rest.community;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 * <p>
 * <ul>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * <li>keyword: 小区关键字</li>
 * </ul>
 *
 */
public class ListComunitiesByKeywordCommand {
	
	private Long pageAnchor;
	
	private Integer pageSize;
	
	@NotNull
	private String keyword;
	
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
	
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
