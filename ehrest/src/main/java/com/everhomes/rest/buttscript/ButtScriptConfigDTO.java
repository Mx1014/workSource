package com.everhomes.rest.buttscript;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 主键(必填)</li>
 * <li>namespaceId: 域空间ID(必填)</li>
 * <li>infoType: 脚本分类(必填)</li>
 * <li>infoDescribe: 描述</li>
 * <li>moduleId: </li>
 * <li>moduleType: </li>
 * <li>status: 状态:0失效;1生效</li>
 * <li>remark: </li>
 * </ul>
 */
public class ButtScriptConfigDTO {

    private Long    id ;
    private Integer   namespaceId ;
    private String   infoType ;
    private String  infoDescribe;
    private Long   moduleId ;
    private String  moduleType ;
    private Byte   status;
    private String remark;

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

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
