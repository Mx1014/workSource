// @formatter:off
package com.everhomes.pm;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>members：pmMember成员信息，参考{@link com.everhomes.pm.PropOwnerDTO}</li>
 * </ul>
 */
public class ListPropOwnerCommandResponse {
	private Long nextPageAnchor;
    private List<PropOwnerDTO> owners;
    
    public ListPropOwnerCommandResponse() {
    }
    
    public ListPropOwnerCommandResponse(Long nextPageAnchor, List<PropOwnerDTO> owners) {
        this.nextPageAnchor = nextPageAnchor;
        this.owners = owners;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PropOwnerDTO> getMembers() {
        return owners;
    }

    public void setMembers(List<PropOwnerDTO> owners) {
        this.owners = owners;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
