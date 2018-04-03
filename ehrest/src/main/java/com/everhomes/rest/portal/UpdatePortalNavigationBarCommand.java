// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 门户导航栏的id</li>
 * <li>label: 门户itemGroup名称</li>
 * <li>description: 门户itemGroup描述</li>
 * <li>targetType: 对象类型</li>
 * <li>targetId: 对象id</li>
 * <li>iconUri: icon 图片</li>
 * <li>selectedIconUri: icon选中图片</li>
 * </ul>
 */
public class UpdatePortalNavigationBarCommand {

	private Long id;

	private String label;

	private String description;

	private String targetType;

	private Long targetId;

	private String iconUri;

	private String selectedIconUri;

	public UpdatePortalNavigationBarCommand() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public String getSelectedIconUri() {
		return selectedIconUri;
	}

	public void setSelectedIconUri(String selectedIconUri) {
		this.selectedIconUri = selectedIconUri;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
