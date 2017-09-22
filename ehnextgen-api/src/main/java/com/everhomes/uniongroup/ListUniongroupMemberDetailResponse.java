package com.everhomes.uniongroup;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>uniongroupMemberDetailList: 关联人员信息</li>
 * <li>pageAnchor: 如果存在下一页，则返回锚点。如果查询到最后一页，则为返回为空</li>
 * <li>pageSize: 每页查询条数</li>
 * </ul>
 */
public class ListUniongroupMemberDetailResponse {
    private List<UniongroupMemberDetail> uniongroupMemberDetailList;
    private Long pageAnchor;
    private Integer pageSize;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<UniongroupMemberDetail> getUniongroupMemberDetailList() {
        return uniongroupMemberDetailList;
    }

    public void setUniongroupMemberDetailList(List<UniongroupMemberDetail> uniongroupMemberDetailList) {
        this.uniongroupMemberDetailList = uniongroupMemberDetailList;
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
}
