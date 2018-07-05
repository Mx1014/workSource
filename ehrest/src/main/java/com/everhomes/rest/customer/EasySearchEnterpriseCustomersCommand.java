//@formatter:off
package com.everhomes.rest.customer;

/**
 * Created by Wentian Wang on 2018/6/14.
 */

public class EasySearchEnterpriseCustomersCommand {
    private String keyWord;
    private Integer namespaceId;
    private Long communityId;

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

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
