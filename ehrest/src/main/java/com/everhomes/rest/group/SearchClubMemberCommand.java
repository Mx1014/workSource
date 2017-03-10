// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>groupId: 俱乐部id</li>
 * <li>keyword: 关键字</li>
 * <li>status: 成员在group里的状态，参考{@link com.everhomes.rest.group.GroupMemberStatus}</li>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class SearchClubMemberCommand {

    private Long groupId;

    private String keyword;

    private Byte status;

    private Long pageAnchor;

    private Integer pageSize;

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
