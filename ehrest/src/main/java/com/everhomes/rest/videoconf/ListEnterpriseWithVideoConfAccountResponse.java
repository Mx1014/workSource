package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>enterpriseConfAccounts: enterpriseConfAccounts信息，参考{@link com.everhomes.rest.videoconf.EnterpriseConfAccountDTO}</li>
 * </ul>
 */
public class ListEnterpriseWithVideoConfAccountResponse {
	
	private Long nextPageAnchor;

    @ItemType(EnterpriseConfAccountDTO.class)
    private List<EnterpriseConfAccountDTO> enterpriseConfAccounts;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<EnterpriseConfAccountDTO> getEnterpriseConfAccounts() {
		return enterpriseConfAccounts;
	}

	public void setEnterpriseConfAccounts(
			List<EnterpriseConfAccountDTO> enterpriseConfAccounts) {
		this.enterpriseConfAccounts = enterpriseConfAccounts;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
