// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>serviceAddressId: 自寄服务地址id</li>
 * <li>expressCompanyId: 快递公司id</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListExpressUserCommand {

	private String ownerType;

	private Long ownerId;

	private Long serviceAddressId;

	private Long expressCompanyId;

	private Long pageAnchor;

	private Integer pageSize;

	public ListExpressUserCommand() {

	}

	public ListExpressUserCommand(String ownerType, Long ownerId, Long serviceAddressId, Long expressCompanyId, Long pageAnchor, Integer pageSize) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.serviceAddressId = serviceAddressId;
		this.expressCompanyId = expressCompanyId;
		this.pageAnchor = pageAnchor;
		this.pageSize = pageSize;
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

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
