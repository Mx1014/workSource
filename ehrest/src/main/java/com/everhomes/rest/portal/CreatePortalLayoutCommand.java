// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间</li>
 *     <li>versionId: versionId</li>
 *     <li>label: 门户layout名称</li>
 *     <li>description: 门户layout描述</li>
 *     <li>layoutTemplateId: 门户layout的模板id</li>
 *     <li>type: 类型 参考{@link com.everhomes.rest.launchpadbase.LayoutType}</li>
 *     <li>bgColor: 背景颜色</li>
 * </ul>
 */
public class CreatePortalLayoutCommand {

	private Integer namespaceId;

	private Long versionId;

	private String label;

	private String description;

	private Long layoutTemplateId;

	private Byte type;

	private Long bgColor;

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

	public Long getVersionId() {
		return versionId;
	}

	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Long getBgColor() {
		return bgColor;
	}

	public void setBgColor(Long bgColor) {
		this.bgColor = bgColor;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
