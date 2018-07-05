package com.everhomes.rest.general_approval;

import java.sql.Timestamp;

/**
 *
 */
public class SearchFormValDTO {
    private static final long serialVersionUID = 910055375L;
    private Long id;
    private Integer namespaceId;
    private Long organizationId;
    private Long ownerId;
    private String ownerType;
    private Long moduleId;
    private String moduleType;
    private Long sourceId;
    private String sourceType;
    private Long formOriginId;
    private Long formVersion;
    private String fieldName;
    private String fieldType;
    private String fieldValue;
    private Timestamp createTime;
    private String stringTag1;
    private String stringTag2;
    private String stringTag3;
    private String stringTag4;
    private String stringTag5;
    private Long integralTag1;
    private Long integralTag2;
    private Long integralTag3;
    private Long integralTag4;
    private Long integralTag5;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
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

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getStringTag1() {
        return stringTag1;
    }

    public void setStringTag1(String stringTag1) {
        this.stringTag1 = stringTag1;
    }

    public String getStringTag2() {
        return stringTag2;
    }

    public void setStringTag2(String stringTag2) {
        this.stringTag2 = stringTag2;
    }

    public String getStringTag3() {
        return stringTag3;
    }

    public void setStringTag3(String stringTag3) {
        this.stringTag3 = stringTag3;
    }

    public String getStringTag4() {
        return stringTag4;
    }

    public void setStringTag4(String stringTag4) {
        this.stringTag4 = stringTag4;
    }

    public String getStringTag5() {
        return stringTag5;
    }

    public void setStringTag5(String stringTag5) {
        this.stringTag5 = stringTag5;
    }

    public Long getIntegralTag1() {
        return integralTag1;
    }

    public void setIntegralTag1(Long integralTag1) {
        this.integralTag1 = integralTag1;
    }

    public Long getIntegralTag2() {
        return integralTag2;
    }

    public void setIntegralTag2(Long integralTag2) {
        this.integralTag2 = integralTag2;
    }

    public Long getIntegralTag3() {
        return integralTag3;
    }

    public void setIntegralTag3(Long integralTag3) {
        this.integralTag3 = integralTag3;
    }

    public Long getIntegralTag4() {
        return integralTag4;
    }

    public void setIntegralTag4(Long integralTag4) {
        this.integralTag4 = integralTag4;
    }

    public Long getIntegralTag5() {
        return integralTag5;
    }

    public void setIntegralTag5(Long integralTag5) {
        this.integralTag5 = integralTag5;
    }

    @Override
    public String toString() {
        return "SearchFormValsDTO{" +
                "id=" + id +
                ", namespaceId=" + namespaceId +
                ", organizationId=" + organizationId +
                ", ownerId=" + ownerId +
                ", ownerType='" + ownerType + '\'' +
                ", moduleId=" + moduleId +
                ", moduleType='" + moduleType + '\'' +
                ", sourceId=" + sourceId +
                ", sourceType='" + sourceType + '\'' +
                ", formOriginId=" + formOriginId +
                ", formVersion=" + formVersion +
                ", fieldName='" + fieldName + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", fieldValue='" + fieldValue + '\'' +
                ", createTime=" + createTime +
                ", stringTag1='" + stringTag1 + '\'' +
                ", stringTag2='" + stringTag2 + '\'' +
                ", stringTag3='" + stringTag3 + '\'' +
                ", stringTag4='" + stringTag4 + '\'' +
                ", stringTag5='" + stringTag5 + '\'' +
                ", integralTag1=" + integralTag1 +
                ", integralTag2=" + integralTag2 +
                ", integralTag3=" + integralTag3 +
                ", integralTag4=" + integralTag4 +
                ", integralTag5=" + integralTag5 +
                '}';
    }
}
