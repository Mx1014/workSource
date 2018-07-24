// @formatter:off
package com.everhomes.rest.aclink;

/**
 * <ul> 查询转发的消息
 * <li>pageAnchor:下一页锚点(CREATE_TIME)</li>
 * <li>pageSize:每页大小</li>
 * </ul>
 */
public class ListAesUserKeyByUserCommand {
	private Long pageAnchor;
    private Integer pageSize;
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
