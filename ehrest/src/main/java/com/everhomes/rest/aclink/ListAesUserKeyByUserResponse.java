// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>aesUserKeys:钥匙列表{@link com.everhomes.rest.aclink.AesUserKeyDTO}</li>
 * <li>nextPageAnchor: 下一页锚点(CREATE_TIME)</li>
 * </ul>
 */
public class ListAesUserKeyByUserResponse {
    @ItemType(AesUserKeyDTO.class)
    List<AesUserKeyDTO> aesUserKeys;
    
    private Long nextPageAnchor;

    public List<AesUserKeyDTO> getAesUserKeys() {
        return aesUserKeys;
    }

    public void setAesUserKeys(List<AesUserKeyDTO> aesUserKeys) {
        this.aesUserKeys = aesUserKeys;
    }
    
    public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
