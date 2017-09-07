// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>hotlineFlag :  业务说明在app端否显示，参考 {@link com.everhomes.rest.express.ExpressShowType}</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class UpdateExpressHotlineFlagCommand {
	private String ownerType;
	private Long ownerId;
	private Byte hotlineFlag;

	public UpdateExpressHotlineFlagCommand() {
	}
	public UpdateExpressHotlineFlagCommand(String ownerType, Long ownerId, Byte hotlineFlag) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.hotlineFlag = hotlineFlag;
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

	public Byte getHotlineFlag() {
		return hotlineFlag;
	}


	public void setHotlineFlag(Byte hotlineFlag) {
		this.hotlineFlag = hotlineFlag;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
