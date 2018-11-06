package com.everhomes.rest.buttscript;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID(必填)</li>
 * <li>infoType: 脚本分类</li>
 * <li>script: 脚本内容</li>
 * <li>commitVersion: 版本号</li>
 * </ul>
 */
public class ScriptDTO {

    private Integer namespaceId;
    private String    infoType ;
    private String    script  ;
    private String    commitVersion  ;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getCommitVersion() {
        return commitVersion;
    }

    public void setCommitVersion(String commitVersion) {
        this.commitVersion = commitVersion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
