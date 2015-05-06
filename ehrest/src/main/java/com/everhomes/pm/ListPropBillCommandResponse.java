// @formatter:off
package com.everhomes.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>members：pmMember成员信息，参考{@link com.everhomes.pm.PropBillDTO}</li>
 * </ul>
 */
public class ListPropBillCommandResponse {
	private Long nextPageAnchor;
	
	@ItemType(PropBillDTO.class)
    private List<PropBillDTO> bills;
    
    public ListPropBillCommandResponse() {
    }
    
    public ListPropBillCommandResponse(Long nextPageAnchor, List<PropBillDTO> bills) {
        this.nextPageAnchor = nextPageAnchor;
        this.bills = bills;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PropBillDTO> getMembers() {
        return bills;
    }

    public void setMembers(List<PropBillDTO> bills) {
        this.bills = bills;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
