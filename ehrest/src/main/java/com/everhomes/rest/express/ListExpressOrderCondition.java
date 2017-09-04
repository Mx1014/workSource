// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>userId: 用户id</li>
 * <li>serviceAddressId: 自寄服务地址id</li>
 * <li>expressCompanyId: 快递公司id</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.express.ExpressOrderStatus}</li>
 * <li>keyword: 关键字</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * <li>sendType: 寄件类型</li>
 * </ul>
 */
public class ListExpressOrderCondition {

	private Integer namespaceId;
	
	private String ownerType;

	private Long ownerId;
	
	private Long userId;

	private Long serviceAddressId;

	private Long expressCompanyId;

	private Byte status;

	private String keyword;

	private Long pageAnchor;

	private Integer pageSize;
	
	private Byte sendType;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public Byte getSendType() {
		return sendType;
	}

	public void setSendType(Byte sendType) {
		this.sendType = sendType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
