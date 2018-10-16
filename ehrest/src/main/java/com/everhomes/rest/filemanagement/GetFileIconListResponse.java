// @formatter:off
package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>返回值:
 * <li>icons: 图标列表 参考{@link com.everhomes.rest.filemanagement.FileIconDTO}</li>
 * </ul>
 */
public class GetFileIconListResponse {

	private List<FileIconDTO> icons;

	public GetFileIconListResponse() {

	}

	public GetFileIconListResponse(List<FileIconDTO> icons) {
		this.icons = icons;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public List<FileIconDTO> getIcons() {
		return icons;
	}

	public void setIcons(List<FileIconDTO> icons) {
		this.icons = icons;
	}
}
