package com.everhomes.rest.buttscript;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID(必填)</li>
 * <li>infoType: 脚本分类(必填)</li>
 * </ul>
 */
public class ButtScriptTestCommand {
    private String eventName ;
    private String phone ;
    private Integer namespaceId ;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}
