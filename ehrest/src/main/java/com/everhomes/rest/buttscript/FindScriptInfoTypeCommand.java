package com.everhomes.rest.buttscript;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID(必填)</li>
 * </ul>
 */
public class FindScriptInfoTypeCommand {
    private  Integer namespaceId ;

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
}
