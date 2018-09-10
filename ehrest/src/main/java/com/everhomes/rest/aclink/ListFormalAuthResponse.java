// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>auths:授权{@link com.everhomes.rest.aclink.AclinkAuthDTO}</li>
 * <li>nextPageAnchor:下一页锚点</li>
 * </ul>
 */
public class ListFormalAuthResponse {
    private Long nextPageAnchor;
    
    @ItemType(AclinkAuthDTO.class)
    private List<AclinkAuthDTO> auths;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<AclinkAuthDTO> getAuths() {
		return auths;
	}

	public void setAuths(List<AclinkAuthDTO> auths) {
		this.auths = auths;
	}
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
