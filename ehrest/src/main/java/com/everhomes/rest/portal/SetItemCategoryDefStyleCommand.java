// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 门户item的id</li>
 * <li>align: align样式</li>
 * <li>defUri: 默认图片uri</li>
 * </ul>
 */
public class SetItemCategoryDefStyleCommand {

	private Long id;

	private String align;

	private String defUri;

	public SetItemCategoryDefStyleCommand() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getDefUri() {
		return defUri;
	}

	public void setDefUri(String defUri) {
		this.defUri = defUri;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
