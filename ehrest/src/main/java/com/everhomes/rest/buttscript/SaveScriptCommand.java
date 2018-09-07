package com.everhomes.rest.buttscript;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID(必填)</li>
 * <li>infoType: 脚本分类</li>
 * <li>publicCode: 是否发布标记 :0 否 ; 1 是  ; 是否发布标记 :0 否 ; 1 是</li>
 * </ul>
 */
public class SaveScriptCommand {

    private Integer namespaceId;
    private String    infoType ;
    private Byte        publishCode  ;
    private String    script     ;

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

    public Byte getPublishCode() {
        return publishCode;
    }

    public void setPublishCode(Byte publishCode) {
        this.publishCode = publishCode;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
