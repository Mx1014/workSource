// @formatter:off
package com.everhomes.rest.servicemoduleapp;

import com.everhomes.rest.module.ServiceModuleAppType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间</li>
 *     <li>organizationId: organizationId</li>
 *     <li>appType: 应用类型，0-oa, 1-园区, 2-服务 参考{@link ServiceModuleAppType}</li>
 *     <li>installFlag: 是否已安装，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>keyword: 搜索字段</li>
 *     <li>pageAnchor: pageAnchor</li>
 *     <li>pageSize: pageSize</li>
 * </ul>
 */
public class ListServiceModuleAppsByOrganizationIdCommand {

	private Integer namespaceId;
	private Long organizationId;
	private Byte appType;
	private Byte installFlag;
	private String keyword;
	private Long pageAnchor;
	private Integer pageSize;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Byte getAppType() {
		return appType;
	}

	public void setAppType(Byte appType) {
		this.appType = appType;
	}

	public Byte getInstallFlag() {
		return installFlag;
	}

	public void setInstallFlag(Byte installFlag) {
		this.installFlag = installFlag;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
