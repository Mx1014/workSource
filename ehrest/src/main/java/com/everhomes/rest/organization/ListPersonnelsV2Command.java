package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Copied by Ryan on 2017/5/18.
 * <ul>
 * <li>organizationId：政府机构id</li>
 * <li>isSignedup: 是否左邻注册用户</li>
 * <li>status: 状态:1-待认证 3-已同意 0-已拒绝</li>
 * <li>pageAnchor: 页码</li>
 * <li>pageSize: 每页大小</li>
 * <li>keywords: 搜索关键词</li>
 * <li>visibleFlag: 是否隐藏</li>
 * <li>filterScopeTypes: 过滤范围类型{@link com.everhomes.rest.organization.FilterOrganizationContactScopeType}</li>
 * </ul>
 */
public class ListPersonnelsV2Command {

    private Long organizationId;

    private Integer pageOffset;

    private Long pageAnchor;

    private Integer pageSize;

    private String keywords;

    private Byte visibleFlag;
    /*
    private Byte isSignedup;
    private Byte status;
    @ItemType(String.class)
    private List<String> filterScopeTypes;
    */

    public ListPersonnelsV2Command() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Byte getVisibleFlag() {
        return visibleFlag;
    }

    public void setVisibleFlag(Byte visibleFlag) {
        this.visibleFlag = visibleFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
