package com.everhomes.rest.uniongroup;

import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/8/4.
 */
public class ListDetailsNotInUniongroupsCommand {

    public ListDetailsNotInUniongroupsCommand() {

    }

    private Long organizaitonId;
    private Integer namespaceId;
    private String contactName;
    private Integer versionCode;
    private Long departmentId;

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

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
