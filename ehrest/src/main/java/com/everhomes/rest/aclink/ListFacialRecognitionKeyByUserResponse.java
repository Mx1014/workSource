// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.aclink.AesUserKeyDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>aesUserKeys:钥匙列表{@link com.everhomes.rest.aclink.AesUserKeyDTO}</li>
 * <li>nextPageAnchor: 下一页锚点(CREATE_TIME)</li>
 * </ul>
 */
public class ListFacialRecognitionKeyByUserResponse {
	@ItemType(AesUserKeyDTO.class)
    private List<AesUserKeyDTO> aesUserKeys;
	
	private Long nextPageAnchor;

    public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<AesUserKeyDTO> getAesUserKeys() {
        return aesUserKeys;
    }

    public void setAesUserKeys(List<AesUserKeyDTO> aesUserKeys) {
        this.aesUserKeys = aesUserKeys;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
