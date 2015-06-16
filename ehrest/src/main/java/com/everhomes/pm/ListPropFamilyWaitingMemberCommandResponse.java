// @formatter:off
package com.everhomes.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.family.FamilyDTO;
import com.everhomes.util.StringHelper;

/**
/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>members：小区待审核家庭成员信息，参考{@link com.everhomes.family.FamilyDTO}</li>
 * <li>totalCount: 总数。（目前只对大多不分库分表的表有效）</li>
 * </ul>
 */
public class ListPropFamilyWaitingMemberCommandResponse {
	private Integer nextPageOffset;
	
	@ItemType(FamilyDTO.class)
    private List<FamilyDTO> members;
    
	private Integer  totalCount; 
	
    public ListPropFamilyWaitingMemberCommandResponse() {
    }
    
    

    public ListPropFamilyWaitingMemberCommandResponse(Integer nextPageOffset,
			List<FamilyDTO> members) {
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



	public List<FamilyDTO> getMembers() {
		return members;
	}



	public void setMembers(List<FamilyDTO> members) {
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
