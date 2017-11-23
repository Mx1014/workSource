// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 门户item的id</li>
 * <li>status: 门户item的状态</li>
 * </ul>
 */
public class SetPortalItemStatusCommand {

	private Long id;

	private Byte status;

	public SetPortalItemStatusCommand() {

	}

	public SetPortalItemStatusCommand(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
