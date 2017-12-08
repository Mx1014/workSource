package com.everhomes.rest.organization;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 *     <li>detailFlag: 是否开启查看园区企业详情开关 0：关闭， 1：开启</li>
 * </ul>
 * Created by ying.xiong on 2017/11/21.
 */
public class SetOrganizationDetailFlagCommand {
    private Integer namespaceId;

    private Long communityId;

    private Byte detailFlag;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Byte getDetailFlag() {
        return detailFlag;
    }

    public void setDetailFlag(Byte detailFlag) {
        this.detailFlag = detailFlag;
    }
}
