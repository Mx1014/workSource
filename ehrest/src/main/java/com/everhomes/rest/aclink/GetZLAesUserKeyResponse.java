// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userKeys:钥匙列表{@link com.everhomes.rest.aclink.AclinkAppUserKeyDTO}</li>
 * </ul>
 */
public class GetZLAesUserKeyResponse {
	List<AclinkAppUserKeyDTO> userKeys;

	public List<AclinkAppUserKeyDTO> getUserKeys() {
		return userKeys;
	}

	public void setUserKeys(List<AclinkAppUserKeyDTO> userKeys) {
		this.userKeys = userKeys;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
