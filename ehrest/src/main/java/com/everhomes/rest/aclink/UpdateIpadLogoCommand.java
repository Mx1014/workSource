// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：设备id</li>
 * <li>name：设备名称</li>
 * <li>doorAccessId：关联门禁id</li>
 * <li>enterStatus：进出标识0出1进</li>
 * </ul>
 */
public class UpdateIpadLogoCommand {

	private Long ownerId;
	private Byte ownerType;
	private String logoUri;
	private String logoUrl;



	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Byte getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(Byte ownerType) {
		this.ownerType = ownerType;
	}

	public String getLogoUri() {
		return logoUri;
	}

	public void setLogoUri(String logoUri) {
		this.logoUri = logoUri;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
