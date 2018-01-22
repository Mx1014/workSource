// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: 域空间</li>
 *     <li>versionId: versionId</li>
 *     <li>moduleApps: 批量添加的模块应用 {@link com.everhomes.rest.portal.CreateServiceModuleApp}</li>
 * </ul>
 */
public class BatchCreateServiceModuleAppCommand {

	private Integer namespaceId;
	private Long versionId;

	@ItemType(CreateServiceModuleApp.class)
	private List<CreateServiceModuleApp> moduleApps;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public List<CreateServiceModuleApp> getModuleApps() {
		return moduleApps;
	}

	public void setModuleApps(List<CreateServiceModuleApp> moduleApps) {
		this.moduleApps = moduleApps;
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
