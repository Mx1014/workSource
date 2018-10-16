package com.everhomes.rest.buttscript;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>id: 主键</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>infoType: 类型</li>
 * <li>infoDescribe: 描述</li>
 * </ul>
 */
public class ScriptInfoTypeDTO {

    private Long    id ;
    private Integer   namespaceId ;
    private String   infoType ;
    private String   infoDescribe ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getInfoDescribe() {
        return infoDescribe;
    }

    public void setInfoDescribe(String infoDescribe) {
        this.infoDescribe = infoDescribe;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
