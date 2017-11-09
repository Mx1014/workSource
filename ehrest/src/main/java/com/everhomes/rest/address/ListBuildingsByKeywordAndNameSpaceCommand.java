package com.everhomes.rest.address;

/**
 * @author sw on 2017/11/1.
 */
public class ListBuildingsByKeywordAndNameSpaceCommand {
    private String keyword;
    private Integer namespaceId;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
