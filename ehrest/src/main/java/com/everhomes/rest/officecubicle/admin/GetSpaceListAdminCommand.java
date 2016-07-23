package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定空间的请求参数
 * <li>keyWords: 查询关键字</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 一页的大小</li> 
 * </ul>
 */
public class GetSpaceListAdminCommand {
	private String keyWords;
	
	private Long pageAnchor;
    
	private Integer pageSize;
	

	public String getKeyWords() {
		return keyWords;
	}


	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
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
