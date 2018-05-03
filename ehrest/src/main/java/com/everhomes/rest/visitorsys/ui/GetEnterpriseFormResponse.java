// @formatter:off
package com.everhomes.rest.visitorsys.ui;

import com.everhomes.rest.visitorsys.VisitorsysFormConfig;
import com.everhomes.util.StringHelper;

/**
  *<ul>
 * <li>formConfig: (选填)表单配置，{@link com.everhomes.rest.visitorsys.VisitorsysFormConfig}</li>
  *</ul>
  */

public class GetEnterpriseFormResponse {
    private VisitorsysFormConfig formConfig;

    public VisitorsysFormConfig getFormConfig() {
        return formConfig;
    }

    public void setFormConfig(VisitorsysFormConfig formConfig) {
        this.formConfig = formConfig;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
