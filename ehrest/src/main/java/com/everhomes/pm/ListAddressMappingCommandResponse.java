// @formatter:off
package com.everhomes.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>members：pmMember成员信息，参考{@link com.everhomes.pm.PropOwnerDTO}</li>
 * <li>pageCount：总页数</li>
 * </ul>
 */
public class ListAddressMappingCommandResponse {
	private Long nextPageAnchor;
	
	@ItemType(PropOwnerDTO.class)
    private List<PropOwnerDTO> owners;
    
	private Integer pageCount;
	
    public ListAddressMappingCommandResponse() {
    }
    
    public ListAddressMappingCommandResponse(Long nextPageAnchor, List<PropOwnerDTO> owners) {
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
    
    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
