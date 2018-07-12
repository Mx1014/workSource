// @formatter:off
package com.everhomes.rest.visitorsys.ui;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.visitorsys.VisitorsysApprovalFormItem;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
  *<ul>
 * <li>formConfig: (选填)表单配置列表，{@link com.everhomes.rest.visitorsys.VisitorsysApprovalFormItem}</li>
  *</ul>
  */

public class GetFormResponse {
    @ItemType(VisitorsysApprovalFormItem.class)
    private List<VisitorsysApprovalFormItem> formConfig;

    public List<VisitorsysApprovalFormItem> getFormConfig() {
        return formConfig;
    }

    public void setFormConfig(List<VisitorsysApprovalFormItem> formConfig) {
        this.formConfig = formConfig;
    }

    public GetFormResponse(List<VisitorsysApprovalFormItem> formConfig) {
        this.formConfig = formConfig;
    }

    public GetFormResponse() {
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
