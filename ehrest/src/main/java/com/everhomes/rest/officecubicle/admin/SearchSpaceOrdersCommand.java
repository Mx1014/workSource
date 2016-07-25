package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定空间的请求参数
 * <li>beginDate: 查询开始时间</li>
 * <li>endDate: 查询结束时间</li>
 * <li>spaceName: 查询空间名</li>
 * <li>reserveKeyword: 查询预订人关键字</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 一页的大小</li> 
 * </ul>
 */
public class SearchSpaceOrdersCommand {
	private Long beginDate;
	private Long endDate;
	private String spaceName;
	private String reserveKeyword;
	private Long pageAnchor;
    
	private Integer pageSize;
	
 

	public Long getBeginDate() {
		return beginDate;
	}


	public void setBeginDate(Long beginDate) {
		this.beginDate = beginDate;
	}


	public Long getEndDate() {
		return endDate;
	}


	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}


	public String getSpaceName() {
		return spaceName;
	}


	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}


	public String getReserveKeyword() {
		return reserveKeyword;
	}


	public void setReserveKeyword(String reserveKeyword) {
		this.reserveKeyword = reserveKeyword;
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


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
