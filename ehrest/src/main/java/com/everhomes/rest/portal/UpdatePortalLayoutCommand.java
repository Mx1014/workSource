// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 门户layout的id</li>
 * <li>label: 门户layout名称</li>
 * <li>description: 门户layout描述</li>
 * </ul>
 */
public class UpdatePortalLayoutCommand {

	private Long id;

	private String label;

	private String description;

	public UpdatePortalLayoutCommand() {

	}

	public UpdatePortalLayoutCommand(Long id, String label, String description) {
		super();
		this.id = id;
		this.label = label;
		this.description = description;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
