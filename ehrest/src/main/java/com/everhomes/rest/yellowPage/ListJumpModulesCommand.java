package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>namespaceId: 域空间id</li>
 *  <li>versionId: 模块的版本id</li>
 * </ul>
 */
public class ListJumpModulesCommand {
	
	@NotNull
	private Integer   namespaceId;
	@NotNull
	private Long   versionId;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}


	public Long getVersionId() {
		return versionId;
	}


	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}
}
