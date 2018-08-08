// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul> 查询转发的消息
 * <li>rightRemote: 是否有远程开门权限0无1有</li>
 * <li>pageAnchor:下一页锚点(CREATE_TIME)</li>
 * <li>pageSize:每页大小</li>
 * </ul>
 *
 */
public class ListAdminAesUserKeyCommand {
	private Byte rightRemote;
	private Long pageAnchor;
    private Integer pageSize;

	public Byte getRightRemote() {
		return rightRemote;
	}

	public void setRightRemote(Byte rightRemote) {
		this.rightRemote = rightRemote;
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
