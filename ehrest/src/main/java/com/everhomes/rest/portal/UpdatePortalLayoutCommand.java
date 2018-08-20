// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 门户layout的id</li>
 *     <li>label: 门户layout名称</li>
 *     <li>description: 门户layout描述</li>
 *     <li>indexFlag: 主页签激活标志0-否，1-是，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class UpdatePortalLayoutCommand {

	private Long id;

	private String label;

	private String description;

	private Byte indexFlag;

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
