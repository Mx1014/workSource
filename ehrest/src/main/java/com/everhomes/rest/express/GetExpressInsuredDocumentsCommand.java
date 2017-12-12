// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>sendType : 寄件类型,寄件类型决定了保价文案, 参考 {@link com.everhomes.rest.express.ExpressSendType}</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class GetExpressInsuredDocumentsCommand {
	private String ownerType;
	private Long ownerId;
	private Byte sendType;
	
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
