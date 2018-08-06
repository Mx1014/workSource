package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 项目id</li>
 *     <li>allSyncFlag: 是否全量同步：0-增量同步，1-全量同步</li>
 * </ul>
 * Created by ying.xiong on 2017/8/30.
 */
public class SyncCustomersCommand {
    private Integer namespaceId;

    private Long communityId;

    private Long orgId;

    private Byte allSyncFlag;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getAllSyncFlag() {
        return allSyncFlag;
    }

    public void setAllSyncFlag(Byte allSyncFlag) {
        this.allSyncFlag = allSyncFlag;
    }
}
