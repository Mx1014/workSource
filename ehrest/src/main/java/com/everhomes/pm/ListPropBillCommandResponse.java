// @formatter:off
package com.everhomes.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>members：物业账单信息，参考{@link com.everhomes.pm.PropBillDTO}</li>
 * <li>totalCount: 总数。（目前只对大多不分库分表的表有效）</li>
 * </ul>
 */
public class ListPropBillCommandResponse {
	private Integer nextPageOffset;
	
	@ItemType(PropBillDTO.class)
    private List<PropBillDTO> members;
    
	private Integer  totalCount; 
	    
    public ListPropBillCommandResponse() {
    }
    
    private Integer pageCount;
    
    public ListPropBillCommandResponse(Integer nextPageOffset, List<PropBillDTO> members) {
        this.nextPageOffset = nextPageOffset;
        this.members = members;
    }

  
    public Integer getNextPageOffset() {
		return nextPageOffset;
	}


	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}


	public List<PropBillDTO> getMembers() {
        return members;
    }

    public void setMembers(List<PropBillDTO> members) {
        this.members = members;
    }
    
    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
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
