// @formatter:off
package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>expressUsers: 添加的快递人员列表</li>
 * </ul>
 */
public class AddExpressUserCommand {

	private String ownerType;

	private Long ownerId;

	@ItemType(CreateExpressUserDTO.class)
	private List<CreateExpressUserDTO> expressUsers;

	public AddExpressUserCommand() {

	}

	public AddExpressUserCommand(String ownerType, Long ownerId, List<CreateExpressUserDTO> expressUsers) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.expressUsers = expressUsers;
	}

	public String getOwnerType() {
		return ownerType;
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

	public List<CreateExpressUserDTO> getExpressUsers() {
		return expressUsers;
	}

	public void setExpressUsers(List<CreateExpressUserDTO> expressUsers) {
		this.expressUsers = expressUsers;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
