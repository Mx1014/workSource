// @formatter:off
package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>members：pmMember成员信息，参考{@link com.everhomes.pm.PropertyMemberDTO}</li>
 * <li>pageCount：总页数</li>
 * </ul>
 */
public class ListPropMemberCommandResponse {
    private Long nextPageAnchor;
    
    @ItemType(PropertyMemberDTO.class)
    private List<PropertyMemberDTO> members;
    
    private Integer pageCount;
    
    public ListPropMemberCommandResponse() {
    }
    
    public ListPropMemberCommandResponse(Long nextPageAnchor, List<PropertyMemberDTO> members) {
        this.nextPageAnchor = nextPageAnchor;
        this.members = members;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PropertyMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<PropertyMemberDTO> members) {
        this.members = members;
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
