// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>id: 热线id</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class DeleteExpressHotlineCommand {

	private String ownerType;
	private Long ownerId;
	private Long id;

	public DeleteExpressHotlineCommand() {
	}

	public String getOwnerType() {
		return ownerType;
	}

	public DeleteExpressHotlineCommand(String ownerType, Long ownerId, Long id) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.id = id;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
