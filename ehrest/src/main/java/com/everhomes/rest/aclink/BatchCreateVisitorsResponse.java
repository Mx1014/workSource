// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>authList:授权信息列表 {@link com.everhomes.rest.aclink.OpenAuthDTO}</li>
 * </ul>
 *
 */
public class BatchCreateVisitorsResponse {
	private List<OpenAuthDTO> authList; 
	 
 
	public List<OpenAuthDTO> getAuthList() {
		return authList;
	}

	public void setAuthList(List<OpenAuthDTO> authList) {
		this.authList = authList;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
 
}
