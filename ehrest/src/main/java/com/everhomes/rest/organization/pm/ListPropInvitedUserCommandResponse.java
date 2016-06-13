// @formatter:off
package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>members：pmMember成员信息，参考{@link com.everhomes.pm.PropInvitedUserDTO}</li>
 * <li>pageCount: 总页数</li>
 * </ul>
 */
public class ListPropInvitedUserCommandResponse {
	private Long nextPageAnchor;
	
	@ItemType(PropInvitedUserDTO.class)
    private List<PropInvitedUserDTO> users;
    
	private Integer pageCount;
	
    public ListPropInvitedUserCommandResponse() {
    }
    
    public ListPropInvitedUserCommandResponse(Long nextPageAnchor, List<PropInvitedUserDTO> users) {
        this.nextPageAnchor = nextPageAnchor;
        this.users = users;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PropInvitedUserDTO> getMembers() {
        return users;
    }

    public void setMembers(List<PropInvitedUserDTO> users) {
        this.users = users;
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
