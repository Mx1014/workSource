// @formatter:off
package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>members：物业地址映射信息，参考{@link com.everhomes.pm.PropAddressMappingDTO}</li>
 * </ul>
 */
public class ListPropAddressMappingCommandResponse {
    private Integer nextPageOffset;
    
    @ItemType(PropAddressMappingDTO.class)
    private List<PropAddressMappingDTO> members;
    
    
    public ListPropAddressMappingCommandResponse() {
    }
    
    public ListPropAddressMappingCommandResponse(Integer nextPageOffset, List<PropAddressMappingDTO> members) {
        this.nextPageOffset = nextPageOffset;
        this.members = members;
    }
    
    public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<PropAddressMappingDTO> getMembers() {
        return members;
    }

    public void setMembers(List<PropAddressMappingDTO> members) {
        this.members = members;
    }
    

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
