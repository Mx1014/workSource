//@formatter:off
package com.everhomes.rest.asset;

/**
 *<ul>
 * <li>isExist: 1：代表true，0：代表false</li>
 *</ul>
 */
public class IsUserExistInAddressResponse {
    private Byte isExist;

	public Byte getIsExist() {
		return isExist;
	}

	public void setIsExist(Byte isExist) {
		this.isExist = isExist;
	}
}
