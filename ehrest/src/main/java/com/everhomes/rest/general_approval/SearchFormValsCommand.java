package com.everhomes.rest.general_approval;


import java.util.List;

/**
 * <ul>
 *     <li>formOriginId: 表单id</li>
 *     <li>formVersion: 表单版本</li>
 *     <li>filteredFields: 过滤的字段列表 {@link com.everhomes.rest.general_approval.SearchGeneralFormItem}</li>
 *     <li>displayFields: 需要显示的列表 {@link com.everhomes.rest.general_approval.SearchGeneralFormItem}</li>
 *     <li>pageAnchor: pageAnchor</li>
 * </ul>
 */
public class SearchFormValsCommand {
    private Long formOriginId;
    private Long formVersion;
    private List<SearchGeneralFormItem> filteredFields;
    private List<SearchGeneralFormItem> displayFields;
    private Long pageAnchor;
    private Integer pageSize;

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
        return "SearchFormValsCommand{" +
                "formOriginId=" + formOriginId +
                ", formVersion=" + formVersion +
                ", filteredFields=" + filteredFields +
                ", displayFields=" + displayFields +
                ", pageAnchor=" + pageAnchor +
                ", pageSize=" + pageSize +
                '}';
    }
}
