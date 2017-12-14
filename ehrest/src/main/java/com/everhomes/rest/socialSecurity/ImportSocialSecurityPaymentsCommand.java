// @formatter:off
package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型 organization</li>
 * <li>ownerId: 所属id 公司id</li>
 * <li>attachment: 文件</li>
 * </ul>
 */
public class ImportSocialSecurityPaymentsCommand {

	private String ownerType;

	private Long ownerId;
 

	public ImportSocialSecurityPaymentsCommand() {

	}

	public ImportSocialSecurityPaymentsCommand(String ownerType, Long ownerId) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId; 
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
  

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
