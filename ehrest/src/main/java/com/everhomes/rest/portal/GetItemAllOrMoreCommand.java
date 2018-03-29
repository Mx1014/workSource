// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>itemGroupId: item group id</li>
 *     <li>namespaceId: 域空间</li>
 *     <li>moreOrAllType: 更多或者全部类型： all(全部),more(更多)</li>
 *     <li>versionId: 与itemGroupId二者必须要有一个</li>
 * </ul>
 */
public class GetItemAllOrMoreCommand {

	private Long itemGroupId;

	private Integer namespaceId;

	private String moreOrAllType;

	private Long versionId;

	public GetItemAllOrMoreCommand() {

	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getMoreOrAllType() {
		return moreOrAllType;
	}

	public void setMoreOrAllType(String moreOrAllType) {
		this.moreOrAllType = moreOrAllType;
	}

	public Long getItemGroupId() {
		return itemGroupId;
	}

	public void setItemGroupId(Long itemGroupId) {
		this.itemGroupId = itemGroupId;
	}

	public Long getVersionId() {
		return versionId;
	}

	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
