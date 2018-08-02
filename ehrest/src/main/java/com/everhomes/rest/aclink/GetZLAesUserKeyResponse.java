// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id:门禁id</li>
 * <li>status: 门禁状态， 0 激活中，1 已激活，2 无效</li>
 * <li>activeUserId: 激活者id</li>
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
