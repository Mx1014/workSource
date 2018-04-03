package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>ownerId: 附件拥有者id</li>
 *     <li>pageAnchor: 锚点 没有则不传</li>
 *     <li>pageSize: 页面大小</li>
 * </ul>
 */
public class ListAttachmentsCommand {

	private Long ownerId;
	
	private Long pageAnchor;

    private Integer pageSize;
    
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
