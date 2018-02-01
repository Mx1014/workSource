package com.everhomes.rest.notice;

import com.everhomes.util.StringHelper;

/**
 * <p>获取企业公告详情信息</p>
 * <ul>
 * <li>organizationId : 公司ID</li>
 * <li>showType: 正常显示公告或者预览，请查看{@link EnterpriseNoticeShowType}</li>
 * <li>id : 要获取的公告ID</li>
 * </ul>
 */
public class GetEnterpriseNoticeCommand {
    private Long organizationId;
    private Byte showType;
    private Long id;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getShowType() {
        return showType;
    }

    public void setShowType(Byte showType) {
        this.showType = showType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
