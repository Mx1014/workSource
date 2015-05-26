// @formatter:off
package com.everhomes.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>members：pmMember成员信息，参考{@link com.everhomes.pm.PropAddressMappingDTO}</li>
 * <li>pageCount：总页数</li>
 * </ul>
 */
public class ListPropAddressMappingCommandResponse {
    private Long nextPageAnchor;
    
    @ItemType(PropAddressMappingDTO.class)
    private List<PropAddressMappingDTO> mappings;
    
    public ListPropAddressMappingCommandResponse() {
    }
    
    private Integer pageCount;
    
    public ListPropAddressMappingCommandResponse(Long nextPageAnchor, List<PropAddressMappingDTO> mappings) {
        this.nextPageAnchor = nextPageAnchor;
        this.mappings = mappings;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PropAddressMappingDTO> getMembers() {
        return mappings;
    }

    public void setMembers(List<PropAddressMappingDTO> mappings) {
        this.mappings = mappings;
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
