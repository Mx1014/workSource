package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>formOriginId: formOriginId</li>
 *     <li>formVersion: formVersion</li>
 *     <li>selectedFormFieldsStr: 被选中的表单字段数组，转换成json后的字符串</li>
 * </ul>
 */
public class FlowFormRelationDataCustomizeField {

    @NotNull
    private Long formOriginId;
    @NotNull
    private Long formVersion;
    private String selectedFormFieldsStr;

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

    public String getSelectedFormFieldsStr() {
        return selectedFormFieldsStr;
    }

    public void setSelectedFormFieldsStr(String selectedFormFieldsStr) {
        this.selectedFormFieldsStr = selectedFormFieldsStr;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
