// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 参数：
 * <li>sceneToken: 场景</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListAskForLeaveRequestBySceneCommand {
	private String sceneToken;
	private Long pageAnchor;
	private Integer pageSize;

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
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
