// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>pageAnchor: 锚点</li>
 *     <li>pageSize: 每页大小</li>
 *     <li>clubType: clubType</li>
 * </ul>
 */
public class ListUserGroupPostCommand {

	private Long pageAnchor;

	private Integer pageSize;

	private Byte clubType;

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

	public Byte getClubType() {
		return clubType;
	}

	public void setClubType(Byte clubType) {
		this.clubType = clubType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
