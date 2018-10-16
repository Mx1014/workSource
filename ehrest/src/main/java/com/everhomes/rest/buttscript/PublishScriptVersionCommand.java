package com.everhomes.rest.buttscript;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID(必填)</li>
 * <li>infoType: 脚本分类(必填)</li>
 * <li>commitVersion: 版本号  (必填)</li>
 * </ul>
 */
public class PublishScriptVersionCommand {

    private Integer namespaceId ;
    private String   infoType ;
    private String   commitVersion  ;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getCommitVersion() {
        return commitVersion;
    }

    public void setCommitVersion(String commitVersion) {
        this.commitVersion = commitVersion;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
