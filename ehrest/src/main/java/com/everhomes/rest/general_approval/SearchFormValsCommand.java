package com.everhomes.rest.general_approval;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>formOriginId: 表单id</li>
 *     <li>formVersion: 表单版本</li>
 *     <li>ownerId[Long]: 将communityId存入ownerId</li>
 *     <li>ownerType[String]: 存入'communityId' </li>
 *     <li>filteredFields: 过滤的字段列表 {@link com.everhomes.rest.general_approval.SearchGeneralFormItem}</li>
 *     <li>displayFields: 需要显示的列表 {@link com.everhomes.rest.general_approval.SearchGeneralFormItem}</li>
 *     <li>pageAnchor: pageAnchor</li>
 * </ul>
 */
public class SearchFormValsCommand {
    private Long approvalId;
    private Long formOriginId;
    private Long formVersion;
    private Long ownerId;
    private String referType;
    private String ownerType;
    private String moduleId;
    private String moduleName;
    private List<SearchGeneralFormItem> filteredFields;
    private List<SearchGeneralFormItem> displayFields;
    private List<SearchGeneralFormItem> conditionFields;
    private Long pageAnchor;
    private Integer pageSize;

    public String getReferType() {
        return referType;
    }

    public void setReferType(String referType) {
        this.referType = referType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
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

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public List<SearchGeneralFormItem> getFilteredFields() {
        return filteredFields;
    }

    public void setFilteredFields(List<SearchGeneralFormItem> filteredFields) {
        this.filteredFields = filteredFields;
    }

    public List<SearchGeneralFormItem> getDisplayFields() {
        return displayFields;
    }

    public void setDisplayFields(List<SearchGeneralFormItem> displayFields) {
        this.displayFields = displayFields;
    }

    public List<SearchGeneralFormItem> getConditionFields() {
        return conditionFields;
    }

    public void setConditionFields(List<SearchGeneralFormItem> conditionFields) {
        this.conditionFields = conditionFields;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
