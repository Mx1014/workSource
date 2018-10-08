package com.everhomes.rest.buttscript;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID(必填)</li>
 * <li>infoType: 脚本分类(必填)</li>
 * <li>eventName: </li>
 * <li>describe: </li>
 * <li>synFlag: </li>
 * <li>id: </li>
 * </ul>
 */
public class ButtInfoTypeEventMappingDTO {

    private  Integer   namespaceId ;
    private  String   infoType ;
    private  String  eventName ;
    private  String   describe ;
    private  Byte   syncFlag;
    private  Long   id ;

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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Byte getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(Byte syncFlag) {
        this.syncFlag = syncFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
