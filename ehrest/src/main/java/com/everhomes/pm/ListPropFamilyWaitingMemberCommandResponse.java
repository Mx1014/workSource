// @formatter:off
package com.everhomes.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.family.FamilyDTO;
import com.everhomes.util.StringHelper;

/**
/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>members：小区待审核家庭成员信息，参考{@link com.everhomes.family.FamilyDTO}</li>
 * </ul>
 */
public class ListPropFamilyWaitingMemberCommandResponse {
	private Long nextPageAnchor;
	
	@ItemType(FamilyDTO.class)
    private List<FamilyDTO> members;
    
    public ListPropFamilyWaitingMemberCommandResponse() {
    }
    
    public ListPropFamilyWaitingMemberCommandResponse(Long nextPageAnchor, List<FamilyDTO> members) {
        this.nextPageAnchor = nextPageAnchor;
        this.members = members;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<FamilyDTO> getMembers() {
        return members;
    }

    public void setMembers(List<FamilyDTO> users) {
        this.members = users;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
