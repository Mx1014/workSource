package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>confAccounts: confAccounts信息，参考{@link com.everhomes.rest.videoconf.ConfAccountDTO}</li>
 * </ul>
 */
public class ListEnterpriseVideoConfAccountResponse {

	private Long nextPageAnchor;

    @ItemType(ConfAccountDTO.class)
    private List<ConfAccountDTO> confAccounts;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ConfAccountDTO> getConfAccounts() {
		return confAccounts;
	}

	public void setConfAccounts(List<ConfAccountDTO> confAccounts) {
		this.confAccounts = confAccounts;
	}
    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
