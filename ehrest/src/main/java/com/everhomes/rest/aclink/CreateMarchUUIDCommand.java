// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>uuidType：配对码类型  0服务器 1ipad</li>
 * </ul>
 */
public class CreateMarchUUIDCommand {
	private Byte uuidType;

	public Byte getUuidType() {
		return uuidType;
	}

	public void setUuidType(Byte uuidType) {
		this.uuidType = uuidType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
