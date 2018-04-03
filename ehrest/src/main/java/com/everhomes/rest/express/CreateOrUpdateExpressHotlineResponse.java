// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>id: 热线id，创建热线不传值，更新需传值</li>
 * <li>serviceName: 服务热线名称</li>
 * <li>hotline: 热线</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class CreateOrUpdateExpressHotlineResponse {
	private String ownerType;
	private Long ownerId;
	private Long id;
	private String serviceName;
	private String hotline;

	public CreateOrUpdateExpressHotlineResponse() {
	}

	public CreateOrUpdateExpressHotlineResponse(String ownerType, Long ownerId, Long id, String serviceName,
			String hotline) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.id = id;
		this.serviceName = serviceName;
		this.hotline = hotline;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getHotline() {
		return hotline;
	}

	public void setHotline(String hotline) {
		this.hotline = hotline;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
