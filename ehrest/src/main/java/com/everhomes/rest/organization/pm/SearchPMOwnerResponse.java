package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>owners: 业主信息 参考{@link com.everhomes.rest.organization.pm.OwnerDTO}</li>
 * </ul>
 */
public class SearchPMOwnerResponse {

	private Long nextPageAnchor;
	
	@ItemType(OwnerDTO.class)
	private List<OwnerDTO> owners;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<OwnerDTO> getOwners() {
		return owners;
	}

	public void setOwners(List<OwnerDTO> owners) {
		this.owners = owners;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
