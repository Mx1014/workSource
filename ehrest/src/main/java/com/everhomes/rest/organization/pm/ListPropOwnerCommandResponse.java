// @formatter:off
package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>members：业主列表信息，参考{@link com.everhomes.rest.organization.pm.PropOwnerDTO}</li>
 * </ul>
 */
public class ListPropOwnerCommandResponse {
	private Integer nextPageOffset;
	
	@ItemType(PropOwnerDTO.class)
    private List<PropOwnerDTO> members;
   
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
