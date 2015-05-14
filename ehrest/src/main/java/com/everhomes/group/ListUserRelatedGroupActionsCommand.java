// @formatter:off
package com.everhomes.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId: group id</li>
 * <li>memberStatus: group成员的状态；为null时则同时查询WAITING_FOR_APPROVAL, WAITING_FOR_ACCEPTANCE两种状态，
 * 		否则查指定状态的待处理列表；其中需要为管理员才能查WAITING_FOR_APPROVAL状态的待处理列表；
 *      参考{@link com.everhomes.group.GroupMemberStatus}</li>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListUserRelatedGroupActionsCommand {
    @NotNull
    private Long groupId;

    private Byte memberStatus;
    
    private Long pageAnchor;
    
    private Integer pageSize;
    
    public ListUserRelatedGroupActionsCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Byte getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(Byte memberStatus) {
		this.memberStatus = memberStatus;
	}

	public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
