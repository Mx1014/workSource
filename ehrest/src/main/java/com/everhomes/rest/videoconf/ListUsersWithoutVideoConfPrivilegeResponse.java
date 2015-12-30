package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>enterpriseUsers: enterpriseUsers信息，参考{@link com.everhomes.rest.videoconf.EnterpriseUsersDTO}</li>
 * </ul>
 */
public class ListUsersWithoutVideoConfPrivilegeResponse {

	private Long nextPageAnchor;
	
	@ItemType(EnterpriseUsersDTO.class)
    private List<EnterpriseUsersDTO> enterpriseUsers;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<EnterpriseUsersDTO> getEnterpriseUsers() {
		return enterpriseUsers;
	}

	public void setEnterpriseUsers(List<EnterpriseUsersDTO> enterpriseUsers) {
		this.enterpriseUsers = enterpriseUsers;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
