package com.everhomes.uniongroup;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
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
