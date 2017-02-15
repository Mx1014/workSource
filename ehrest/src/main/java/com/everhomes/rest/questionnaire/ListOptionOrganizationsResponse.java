// @formatter:off
package com.everhomes.rest.questionnaire;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>organizationNames: 企业名称列表</li>
 * </ul>
 */
public class ListOptionOrganizationsResponse {

	private Long nextPageAnchor;

	@ItemType(String.class)
	private List<String> organizationNames;

	public ListOptionOrganizationsResponse() {

	}

	public ListOptionOrganizationsResponse(Long nextPageAnchor, List<String> organizationNames) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.organizationNames = organizationNames;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<String> getOrganizationNames() {
		return organizationNames;
	}

	public void setOrganizationNames(List<String> organizationNames) {
		this.organizationNames = organizationNames;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
