// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间</li>
 * <li>label: 门户layout名称</li>
 * <li>description: 门户layout描述</li>
 * <li>layoutTemplateId: 门户layout的模板id</li>
 * </ul>
 */
public class CreatePortalLayoutCommand {

	private Integer namespaceId;

	private String label;

	private String description;

	private Long layoutTemplateId;

	public CreatePortalLayoutCommand() {

	}

	public CreatePortalLayoutCommand(Integer namespaceId, String label, String description, Long layoutTemplateId) {
		super();
		this.namespaceId = namespaceId;
		this.label = label;
		this.description = description;
		this.layoutTemplateId = layoutTemplateId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getLayoutTemplateId() {
		return layoutTemplateId;
	}

	public void setLayoutTemplateId(Long layoutTemplateId) {
		this.layoutTemplateId = layoutTemplateId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
