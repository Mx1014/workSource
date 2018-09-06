// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>versionId: versionId</li>
 *     <li>type: layout类型，1-首页、2-自定义门户、3-分页签门户，参考{@link PortalLayoutType}</li>
 * </ul>
 */
public class FindIndexPortalLayoutCommand {

	private Long versionId;

	private Byte type;

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
