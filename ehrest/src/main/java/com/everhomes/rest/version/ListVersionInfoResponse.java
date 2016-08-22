// @formatter:off
package com.everhomes.rest.version;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * 
 * <ul>
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>versionList: 版本列表，参考{@link com.everhomes.rest.version.VersionInfoDTO}</li>
 * </ul>
 */
public class ListVersionInfoResponse {
	private Long nextPageAnchor;
	@ItemType(VersionInfoDTO.class)
	private List<VersionInfoDTO> versionList;
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<VersionInfoDTO> getVersionList() {
		return versionList;
	}
	public void setVersionList(List<VersionInfoDTO> versionList) {
		this.versionList = versionList;
	}
	
}
