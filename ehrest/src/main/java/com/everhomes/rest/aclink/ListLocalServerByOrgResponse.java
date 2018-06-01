// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>listServer:内网服务器列表{@link com.everhomes.rest.aclink.AclinkServerDTO}</li>
 * </ul>
 * @author liuyilin
 *
 */
public class ListLocalServerByOrgResponse {
	private List<AclinkServerDTO> listServer;

	public List<AclinkServerDTO> getListServer() {
		return listServer;
	}

	public void setListServer(List<AclinkServerDTO> listServer) {
		this.listServer = listServer;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
