package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>orderAccounts: orderAccounts信息，参考{@link com.everhomes.rest.videoconf.ConfOrderAccountDTO}</li>
 * </ul>
 */
public class ListConfOrderAccountResponse {

	private Long nextPageAnchor;
	
	@ItemType(ConfOrderAccountDTO.class)
    private List<ConfOrderAccountDTO> orderAccounts;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ConfOrderAccountDTO> getOrderAccounts() {
		return orderAccounts;
	}

	public void setOrderAccounts(List<ConfOrderAccountDTO> orderAccounts) {
		this.orderAccounts = orderAccounts;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
