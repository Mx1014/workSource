package com.everhomes.rest.pmNotify;

/**
 * Created by ying.xiong on 2017/9/15.
 */
public class DeletePmNotifyParamsCommand {
    private Long id;

    private Integer namespaceId;

    private Long communityId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
