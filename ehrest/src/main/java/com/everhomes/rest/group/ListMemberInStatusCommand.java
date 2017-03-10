// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>groupId: group id</li>
 * <li>status: 成员在group里的状态，参考{@link com.everhomes.rest.group.GroupMemberStatus}</li>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * <li>includeCreator: 是否包含创建者，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>keyword: 关键字搜索</li>
 * </ul>
 */
public class ListMemberInStatusCommand {
    @NotNull
    private Long groupId;
    
    private Byte status;

    private Long pageAnchor;
    
    private Integer pageSize;
    
    private Byte includeCreator;
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ListMemberInStatusCommand() {
    }

    public Byte getIncludeCreator() {
		return includeCreator;
	}

	public void setIncludeCreator(Byte includeCreator) {
		this.includeCreator = includeCreator;
	}

	public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
