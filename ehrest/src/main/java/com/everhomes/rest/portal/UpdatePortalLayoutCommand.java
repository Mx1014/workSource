// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 门户layout的id</li>
 * <li>name: 门户layout名称</li>
 * <li>description: 门户layout描述</li>
 * </ul>
 */
public class UpdatePortalLayoutCommand {

	private Long id;

	private String name;

	private String description;

	public UpdatePortalLayoutCommand() {

	}

	public UpdatePortalLayoutCommand(Long id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
