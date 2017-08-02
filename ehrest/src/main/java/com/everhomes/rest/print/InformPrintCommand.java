// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>ownerType : 打印所属类型，此处为community, 参考{@link com.everhomes.rest.print.PrintOwnerType}</li>
 * <li>ownerId : 所属id</li>
 * <li>identifierToken : getPrintLogonUrl 接口返回的 url后跟的唯一标识</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class InformPrintCommand {
	private String ownerType;
	private Long ownerId;
	private String identifierToken;

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

	public String getIdentifierToken() {
		return identifierToken;
	}

	public void setIdentifierToken(String identifierToken) {
		this.identifierToken = identifierToken;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}

