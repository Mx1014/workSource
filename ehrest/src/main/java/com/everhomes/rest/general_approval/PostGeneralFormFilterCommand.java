package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>namespaceId : namespaceId</li>
 *     <li>ownerId : 当前项目的id，communityId</li>
 *     <li>ownerType : "community"</li>
 *     <li>moduleId : 当前应用id</li>
 *     <li>moduleType : 当前应用类型</li>
 *     <li>formOriginId : 当前生效的表单Id</li>
 *     <li>formVersion : 当前生效的表单version</li>
 *     <li>formFields : 当前选中的所有筛选字段</li>
 * </ul>
 */
public class PostGeneralFormFilterCommand {
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private Long moduleId;
    private String muduleType;
    private Long formOriginId;
    private Long formVersion;

    @ItemType(GeneralFormFieldDTO.class)
    private List<GeneralFormFieldDTO> formFields;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getMuduleType() {
        return muduleType;
    }

    public void setMuduleType(String muduleType) {
        this.muduleType = muduleType;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }

    public List<GeneralFormFieldDTO> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<GeneralFormFieldDTO> formFields) {
        this.formFields = formFields;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
