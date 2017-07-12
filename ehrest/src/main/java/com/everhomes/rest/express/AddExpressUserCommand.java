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
 * <li>serviceAddressId: 自寄服务地址id</li>
 * <li>expressCompanyId: 快递公司id</li>
 * <li>expressUsers: 添加的快递人员列表，参考{@link com.everhomes.rest.express.CreateExpressUserDTO}</li>
 * </ul>
 */
public class AddExpressUserCommand {

	private String ownerType;

	private Long ownerId;

	private Long serviceAddressId;

	private Long expressCompanyId;

	@ItemType(CreateExpressUserDTO.class)
	private List<CreateExpressUserDTO> expressUsers;

	public AddExpressUserCommand() {

	}

	public AddExpressUserCommand(String ownerType, Long ownerId, Long serviceAddressId, Long expressCompanyId,
			List<CreateExpressUserDTO> expressUsers) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.serviceAddressId = serviceAddressId;
		this.expressCompanyId = expressCompanyId;
		this.expressUsers = expressUsers;
	}

	public Long getServiceAddressId() {
		return serviceAddressId;
	}

	public void setServiceAddressId(Long serviceAddressId) {
		this.serviceAddressId = serviceAddressId;
	}

	public Long getExpressCompanyId() {
		return expressCompanyId;
	}

	public void setExpressCompanyId(Long expressCompanyId) {
		this.expressCompanyId = expressCompanyId;
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
