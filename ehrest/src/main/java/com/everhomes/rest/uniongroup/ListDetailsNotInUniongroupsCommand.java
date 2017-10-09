package com.everhomes.rest.uniongroup;

import com.everhomes.util.StringHelper;

/**
 * <li>organizaitonId 公司id</li>
 * <li>namespaceId 域空间</li>
 * <li>contactName 联系人</li>
 */
public class ListDetailsNotInUniongroupsCommand {

    public ListDetailsNotInUniongroupsCommand() {

    }

    private Long organizaitonId;
    private Integer namespaceId;
    private String contactName;

    public Long getOrganizaitonId() {
        return organizaitonId;
    }

    public void setOrganizaitonId(Long organizaitonId) {
        this.organizaitonId = organizaitonId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
