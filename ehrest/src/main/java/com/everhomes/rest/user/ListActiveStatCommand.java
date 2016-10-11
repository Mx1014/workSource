package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>beginDate:统计开始日期</li>
 *  <li>endDate:统计结束日期</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListActiveStatCommand {
	
	private Long beginDate;
	
	private Long endDate;

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


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

}
