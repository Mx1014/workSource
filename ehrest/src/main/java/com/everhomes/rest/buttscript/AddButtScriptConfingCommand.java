package com.everhomes.rest.buttscript;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>infoType: 分类</li>
 * <li>infoDescribe: 分类描述</li>
 * <li>moduleId: </li>
 * <li>moduleType: </li>
 * <li>status: 生效失效状态:0失效;1生效</li>
 * <li>remark: 备注</li>
 * </ul>
 */
public class AddButtScriptConfingCommand {

    private Integer   namespaceId ;
    private String   infoType ;
    private String  infoDescribe;
    private Long   moduleId ;
    private String  moduleType ;
    private Byte   status;
    private String remark ;

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
