package com.everhomes.rest.user.admin;

import com.everhomes.util.StringHelper;


/**
 * <p>
 * <ul>
 * <li>anchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListInvitatedUserCommand {

	private Long anchor;
    
    private Integer pageSize;
    
    public ListInvitatedUserCommand() {
    }

    public Long getAnchor() {
		return anchor;
	}

	public void setAnchor(Long anchor) {
		this.anchor = anchor;
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
