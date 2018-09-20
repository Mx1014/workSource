// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间</li>
 *     <li>versionId: versionId</li>
 *     <li>label: 门户layout名称</li>
 *     <li>description: 门户layout描述</li>
 *     <li>type: layout类型，1-首页，2-自定义门户，3-分页签门户，参考{@link PortalLayoutType}</li>
 *     <li>indexFlag: 0-否，1-是，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>type: 类型 参考{@link com.everhomes.rest.launchpadbase.LayoutType}</li>
 *     <li>bgColor: 背景颜色</li>
 * </ul>
 */
public class CreatePortalLayoutCommand {

	private Integer namespaceId;

	private Long versionId;

	private String label;

	private String description;

	private Byte type;

	private Byte indexFlag;

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
	public Byte getIndexFlag() {
		return indexFlag;
	}

	public void setIndexFlag(Byte indexFlag) {
		this.indexFlag = indexFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
