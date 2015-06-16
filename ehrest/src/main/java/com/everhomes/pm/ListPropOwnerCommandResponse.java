// @formatter:off
package com.everhomes.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>members：物业账单信息，参考{@link com.everhomes.pm.PropOwnerDTO}</li>
 * <li>totalCount: 总数。（目前只对大多不分库分表的表有效）</li>
 * </ul>
 */
public class ListPropOwnerCommandResponse {
	private Integer nextPageOffset;
	
	@ItemType(PropOwnerDTO.class)
    private List<PropOwnerDTO> members;
    
	private Integer  totalCount; 
	
    public ListPropOwnerCommandResponse() {
    }
    
    
    public ListPropOwnerCommandResponse(Integer nextPageOffset,List<PropOwnerDTO> members) {
		super();
		this.nextPageOffset = nextPageOffset;
		this.members = members;
	}


	public Integer getNextPageOffset() {
		return nextPageOffset;
	}


	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}


	public List<PropOwnerDTO> getMembers() {
		return members;
	}


	public void setMembers(List<PropOwnerDTO> members) {
		this.members = members;
	}


	public Integer getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
