package com.everhomes.rest.general_approval;


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
    private SearchGeneralFormItem filteredFields;
    private SearchGeneralFormItem displayFields;
    private Long pageAnchor;

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

    public SearchGeneralFormItem getFilteredFields() {
        return filteredFields;
    }

    public void setFilteredFields(SearchGeneralFormItem filteredFields) {
        this.filteredFields = filteredFields;
    }

    public SearchGeneralFormItem getDisplayFields() {
        return displayFields;
    }

    public void setDisplayFields(SearchGeneralFormItem displayFields) {
        this.displayFields = displayFields;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    @Override
    public String toString() {
        return "SearchFormValsCommand{" +
                "formOriginId=" + formOriginId +
                ", formVersion=" + formVersion +
                ", filteredFields=" + filteredFields +
                ", displayFields=" + displayFields +
                ", pageAnchor=" + pageAnchor +
                '}';
    }
}
